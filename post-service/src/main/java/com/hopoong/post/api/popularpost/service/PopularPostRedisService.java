package com.hopoong.post.api.popularpost.service;

import com.hopoong.post.domain.Post;

import java.util.List;

public interface PopularPostRedisService {

    void incrementRealTimePopularPostCount(Long postId);

    List<Long> getTopRealTimePopularPosts(int limit);

    void initRealTimePopularPostCount();

    List<Post> getPostsFromCache(List<Long> topPopularPostIds);

    void savePostsToCache(List<Post> dbPosts);

}




