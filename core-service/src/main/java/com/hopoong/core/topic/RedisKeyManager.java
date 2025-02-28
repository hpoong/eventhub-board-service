package com.hopoong.core.topic;

public class RedisKeyManager {

    // 실시간 인기 게시글
    public static final String POPULAR_POST_KEY = "popular:posts:zset";

    // 실시간 인기 게시글 ::: 게시글 정보 캐시
    public static final String CACHE_KEY_POST_DETAIL = "cache:post:detail:";
}
