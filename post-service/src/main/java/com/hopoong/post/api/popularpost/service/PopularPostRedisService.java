package com.hopoong.post.api.popularpost.service;

import java.util.List;

public interface PopularPostRedisService {

    void incrementRealTimePopularPostCount(Long postId);

    List<Long> getTopRealTimePopularPosts(int limit);

    void initRealTimePopularPostCount();
}




