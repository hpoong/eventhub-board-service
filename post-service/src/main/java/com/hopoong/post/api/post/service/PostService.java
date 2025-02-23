package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostModel;

public interface PostService {
    void create(PostModel.CreateRequest createRequest);

    void update(PostModel.UpdateRequest updateRequest);

    void delete(Long postId);
}



