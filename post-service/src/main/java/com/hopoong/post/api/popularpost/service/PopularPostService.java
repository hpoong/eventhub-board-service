package com.hopoong.post.api.popularpost.service;

import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    private static final String POPULAR_POSTS_KEY = "popular_posts"; // Redis Key
    private static final Duration CACHE_TTL = Duration.ofMinutes(45);


    @Transactional(readOnly = true)
    public List<PopularPostModel.TrendingPostModel> findTop100ByOrderByViewsDesc() {
        List<Object[]> top100ByOrderByViewsDesc = postJpaRepository.findTop100ByOrderByViewsDesc();

        // id 0; category_id 1; content 2; created_at 3; title 4; updated_at 5; user_id 6; views 7; rn 8
        return Optional.ofNullable(top100ByOrderByViewsDesc)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(row -> new PopularPostModel.TrendingPostModel(
                        ((Number) row[0]).longValue(),   // id
                        ((Number) row[6]).longValue(),   // userId
                        (String) row[4],                 // title
                        (String) row[2],                 // content
                        ((Number) row[1]).longValue(),   // categoryId
                        ((Number) row[7]).intValue(),    // views
                        ((Number) row[8]).longValue()    // rn
                ))
                .collect(Collectors.toList());
    }



    public void cachePopularPosts() {
        List<PopularPostModel.TrendingPostModel> sortedData = this.findTop100ByOrderByViewsDesc();
        redisTemplate.opsForValue().set(POPULAR_POSTS_KEY, sortedData, CACHE_TTL);
    }


    public List<PopularPostModel.TrendingPostModel> getPopularPostsFromCache() {
        return (List<PopularPostModel.TrendingPostModel>) redisTemplate.opsForValue().get(POPULAR_POSTS_KEY);
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