package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostModel;

public interface PostService {
    void createPost(PostModel.CreateRequest createRequest);

    void findPostById(Long postId);

    void addComment(Long postId);

    void likePost(Long postId);
}



