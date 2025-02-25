package com.hopoong.post.api.popularpost.service;

import com.hopoong.core.topic.RedisKeyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PopularPostRedisServiceImpl implements PopularPostRedisService {


    private final RedisTemplate<String, Object> redisTemplate;

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
                .collect(Collectors.toList()); // Collectors.toList() 로 변경
    }

    @Override
    public void initRealTimePopularPostCount() {
        redisTemplate.delete(RedisKeyManager.POPULAR_POST_KEY);

        redisTemplate.opsForZSet().add(RedisKeyManager.POPULAR_POST_KEY, "init", 0);
        redisTemplate.expire(RedisKeyManager.POPULAR_POST_KEY, Duration.ofMinutes(40));
    }


}
