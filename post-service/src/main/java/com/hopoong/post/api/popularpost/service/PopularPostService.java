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
        List<Post> top100ByOrderByViewsDesc = postJpaRepository.findTop100ByOrderByViewsDesc();

        return Optional.ofNullable(top100ByOrderByViewsDesc)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(post -> new PopularPostModel.TrendingPostModel(
                        post.getId(),
                        post.getUserId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getCategoryId(),
                        post.getViews()
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