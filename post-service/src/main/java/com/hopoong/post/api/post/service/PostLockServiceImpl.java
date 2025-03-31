package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.api.post.repository.PostMetadataJpaRepository;
import com.hopoong.post.domain.PostEntity;
import com.hopoong.post.domain.PostMetadataEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostLockServiceImpl implements PostLockService {

    private final PostMetadataJpaRepository postMetadataJpaRepository;
    private final PostJpaRepository postJpaRepository;


    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<PostMetadataEntity> getPostMetadataByIdWithLock(Long postId) {
        return postMetadataJpaRepository.findById(postId);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PostEntity getPostByIdWithLock(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: %s".formatted(postId)));
    }
}
