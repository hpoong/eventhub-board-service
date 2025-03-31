package com.hopoong.post.api.post.service;

import com.hopoong.post.domain.PostEntity;
import com.hopoong.post.domain.PostMetadataEntity;

import java.util.Optional;

public interface PostLockService {
    PostEntity getPostByIdWithLock(Long postId);

    Optional<PostMetadataEntity> getPostMetadataByIdWithLock(Long postId);
}
