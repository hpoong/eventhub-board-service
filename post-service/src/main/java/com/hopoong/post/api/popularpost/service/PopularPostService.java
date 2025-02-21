package com.hopoong.post.api.popularpost.service;

import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularPostService {

    private final PostJpaRepository postJpaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

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


}