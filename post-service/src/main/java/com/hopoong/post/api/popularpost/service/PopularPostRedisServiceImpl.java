package com.hopoong.post.api.popularpost.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.topic.RedisKeyManager;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PopularPostRedisServiceImpl implements PopularPostRedisService {

    /*
     * 인기 게시글은 TTL 시간은 40분 설정
     * 배치가 reset 하는 시간은 30분
     */

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void incrementRealTimePopularPostCount(Long postId) {
        redisTemplate.opsForZSet().incrementScore(RedisKeyManager.POPULAR_POST_KEY, postId.toString(), 1);
    }

    @Override
    public List<Long> getTopRealTimePopularPosts(int limit) {
        Set<Object> dataModel = redisTemplate.opsForZSet().reverseRange(RedisKeyManager.POPULAR_POST_KEY, 0, limit - 1);
        return dataModel.stream()
                .filter(data -> !"init".equals(data))
                .map(Object::toString)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public void initRealTimePopularPostCount() {
        redisTemplate.delete(RedisKeyManager.POPULAR_POST_KEY);

        redisTemplate.opsForZSet().add(RedisKeyManager.POPULAR_POST_KEY, "init", 0);
        redisTemplate.expire(RedisKeyManager.POPULAR_POST_KEY, Duration.ofMinutes(40));
    }

    @Override
    public List<Post> getPostsFromCache(List<Long> postIds) {
        return postIds.stream()
                .map(id -> redisTemplate.opsForValue().get(RedisKeyManager.CACHE_KEY_POST_DETAIL + id))
                .filter(Objects::nonNull)
                .map(data -> objectMapper.convertValue(data, Post.class))
                .collect(Collectors.toList());
    }

    @Override
    public void savePostsToCache(List<Post> posts) {
        posts.forEach(post -> {
            redisTemplate.opsForValue().set(RedisKeyManager.CACHE_KEY_POST_DETAIL + post.getId(), post, Duration.ofMinutes(40));
        });
    }



}
