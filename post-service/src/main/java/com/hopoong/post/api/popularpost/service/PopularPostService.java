package com.hopoong.post.api.popularpost.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.topic.RedisKeyManager;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PopularPostService {

    private final PostJpaRepository postJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PopularPostRedisService postRedisService;
    private final PopularPostRabbitMQService popularPostRabbitMQService;
    private final ObjectMapper objectMapper;

    private static final Duration CACHE_TTL = Duration.ofMinutes(45);



    /*
     * 카테고리 별 인기글 조회
     */
    @Transactional(readOnly = true)
    public List<PopularPostModel.TrendingPostModel> findTop100ByOrderByViewsDesc() {
        List<Object[]> top100ByOrderByViewsDesc = postJpaRepository.findTop100ByOrderByViewsDesc();

        return Optional.ofNullable(top100ByOrderByViewsDesc)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(row -> new PopularPostModel.TrendingPostModel(
                        ((Number) row[0]).longValue(),   // id
                        ((Number) row[1]).longValue(),   // userId
                        (String) row[2],                 // title
                        (String) row[3],                 // content
                        ((Number) row[4]).longValue(),   // categoryId
                        ((Number) row[5]).intValue(),    // views
                        ((Number) row[6]).longValue()    // rn
                ))
                .collect(Collectors.toList());
    }



    /*
     * 카테고리 별 인기글 등록
     */
    public void saveTrendingPostsByCategory() throws JsonProcessingException {
        List<PopularPostModel.TrendingPostModel> sortedData = this.findTop100ByOrderByViewsDesc();

        // Redis에 저장할 데이터 초기화
        Map<Long, List<PopularPostModel.TrendingPostModel>> categoryPostsMap = new HashMap<>();

        // 카테고리별로 게시글을 그룹화
        for (PopularPostModel.TrendingPostModel post : sortedData) {
            categoryPostsMap
                    .computeIfAbsent(post.categoryId(), k -> new ArrayList<>())
                    .add(post);
        }

        // 카테고리별로 Redis 저장
        for (Map.Entry<Long, List<PopularPostModel.TrendingPostModel>> entry : categoryPostsMap.entrySet()) {
            String redisKey = RedisKeyManager.POPULAR_POSTS_KEY + entry.getKey();

            redisTemplate.delete(redisKey);
            redisTemplate.opsForList().rightPushAll(redisKey, objectMapper.writeValueAsString(entry.getValue())); // 리스트 저장
        }
    }

    /*
     * 카테고리 별 인기글 조회
     */
    public List<PopularPostModel.TrendingPostModel> getTrendingPostsByCategory(Long categoryId) throws JsonProcessingException {
        String redisKey = RedisKeyManager.POPULAR_POSTS_KEY + categoryId;

        // Redis에서 데이터 조회 (Object → String 변환)
        List<Object> cachedData = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (cachedData == null || cachedData.isEmpty()) {
            return Collections.emptyList();
        }

        // JSON → 객체 변환
        List<PopularPostModel.TrendingPostModel> result = new ArrayList<>();
        for (Object data : cachedData) {
            result.addAll(
                    objectMapper.readValue(
                            data.toString(),
                            new TypeReference<List<PopularPostModel.TrendingPostModel>>() {}
                    )
            );
        }

        return result;
    }

    /*
     * 실시간 인기 게시글
     */
    public List<Post> getRealTimeTopPopularPosts(int limit) {
        List<Long> topPopularPostIds = postRedisService.getTopRealTimePopularPosts(limit);

        // Redis에서 먼저 조회
        List<Post> cachedPosts = postRedisService.getPostsFromCache(topPopularPostIds);

        // 캐시되지 않은 ID 추출
        List<Long> missingPostIds = topPopularPostIds.stream()
                .filter(id -> cachedPosts.stream().noneMatch(post -> post.getId().equals(id)))
                .collect(Collectors.toList());

        List<Post> dbPosts = new ArrayList<>();

        // DB에서 조회 후 Redis에 저장
        if (!missingPostIds.isEmpty()) {
            dbPosts = postJpaRepository.findAllById(missingPostIds);
            postRedisService.savePostsToCache(dbPosts);
        }

        // Redis + DB 결과 합쳐서 반환
        return Stream.concat(cachedPosts.stream(), dbPosts.stream())
                .collect(Collectors.toList());
    }


    /*
     * 1시간 마다 사용자 행동 패턴 게시글 집계 처리
     */
    public void aggregatePostMetrics() {
        popularPostRabbitMQService.aggregatePostMetrics();
    }


}