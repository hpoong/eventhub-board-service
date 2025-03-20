package com.hopoong.post.api.popularpost.service;

import com.hopoong.post.domain.PostEntity;

import java.util.List;

public interface PopularPostRedisService {

    void incrementRealTimePopularPostCount(Long postId);

    List<Long> getTopRealTimePopularPosts(int limit);

    void initRealTimePopularPostCount();

    List<PostEntity> getPostsFromCache(List<Long> topPopularPostIds);

    void savePostsToCache(List<PostEntity> dbPostEntities);

}




