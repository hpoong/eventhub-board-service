package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostModel;

public interface PostService {
    void createPost(PostModel.CreateRequest createRequest);

    void deletePost(PostModel.DeleteRequest deleteRequest);

    PostModel.InfoResponse findPostWithTracking(Long postId);

    PostModel.InfoResponse fetchPost(Long postId);

    void addComment(Long postId, PostModel.CommentsCreateRequest request);

    void likePost(Long postId);
}



