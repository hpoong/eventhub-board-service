package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostUsecaseImpl implements PostUsecase {

    private final ApplicationEventPublisher eventPublisher;
    private final PostJpaRepository postJpaRepository;


    @Override
    @Transactional
    public void create(PostModel.CreateRequest createRequest) {
        Post postEntity = Post.createPost(createRequest.userId(), createRequest.title(), createRequest.content(), createRequest.categoryId());
        postJpaRepository.save(postEntity);
        eventPublisher.publishEvent(createRequest);
    }

    @Override
    @Transactional
    public void update(PostModel.UpdateRequest updatePost) {
        eventPublisher.publishEvent(updatePost.toString());
    }

    @Override
    @Transactional
    public void delete(Long postId) {
        eventPublisher.publishEvent(String.valueOf(postId));
    }

}
