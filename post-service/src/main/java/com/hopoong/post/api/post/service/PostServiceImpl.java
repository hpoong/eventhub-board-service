package com.hopoong.post.api.post.service;

import com.hopoong.core.model.PopularPostModel;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import com.hopoong.post.event.PostEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
        postEventHandler.handlePostCountEvent("VIEWED", new PopularPostModel.CommentRabbitMQModel(postId, getRandomLong()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);

    }

    @Override
    public void addComment(Long postId) {
        postEventHandler.handlePostCountEvent("COMMENT", new PopularPostModel.CommentRabbitMQModel(postId, getRandomLong()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }

    @Override
    public void likePost(Long postId) {
        postEventHandler.handlePostCountEvent("LIKED", new PopularPostModel.CommentRabbitMQModel(postId, getRandomLong()));
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }

    public static long getRandomLong() {
        return ThreadLocalRandom.current().nextLong(1, 11);
    }


}
