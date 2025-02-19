package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.domain.Post;

public interface PostUsecase {
    void create(PostModel.CreateRequest createRequest);

    void update(PostModel.UpdateRequest updateRequest);

    void delete(Long postId);
}



