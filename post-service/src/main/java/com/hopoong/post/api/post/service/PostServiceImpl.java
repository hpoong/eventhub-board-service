package com.hopoong.post.api.post.service;

import com.hopoong.core.model.PopularPostModel;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import com.hopoong.post.event.PostEventHandler;
import com.hopoong.post.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ApplicationEventPublisher eventPublisher;
    private final PostJpaRepository postJpaRepository;
    private final PostEventHandler postEventHandler;
    private final PopularPostRedisService popularPostRedisService;


    @Override
    @Transactional
    public void createPost(PostModel.CreateRequest createRequest) {
        Random random = new Random();
        int randomNumber = random.nextInt(100000) + 1;
        Post postEntity = Post.createPost(createRequest.userId(), createRequest.title(), createRequest.content(), createRequest.categoryId(), randomNumber);
        postJpaRepository.save(postEntity);
        eventPublisher.publishEvent(createRequest);
    }

    @Override
    public void findPostById(Long postId) {
        postEventHandler.handleUserBehaviorEvent("VIEWED", new PopularPostModel.PostUserBehaviorMessageModel(postId, RandomUtil.getRandomUserId()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);

    }

    @Override
    public void addComment(Long postId) {
        postEventHandler.handleUserBehaviorEvent("COMMENT", new PopularPostModel.PostUserBehaviorMessageModel(postId, RandomUtil.getRandomUserId()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }

    @Override
    public void likePost(Long postId) {
        postEventHandler.handleUserBehaviorEvent("LIKED", new PopularPostModel.PostUserBehaviorMessageModel(postId, RandomUtil.getRandomUserId()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }
}
