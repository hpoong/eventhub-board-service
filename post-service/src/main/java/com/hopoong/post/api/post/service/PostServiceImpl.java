package com.hopoong.post.api.post.service;

import com.hopoong.core.model.popularpost.PostUserBehaviorMessage;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.domain.Post;
import com.hopoong.post.event.PostEventHandler;
import com.hopoong.post.util.RandomUtil;
import jakarta.persistence.EntityNotFoundException;
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

        // kafka ::: 포인트 등록
        eventPublisher.publishEvent(postEntity);
    }




    @Override
    @Transactional
    public void deletePost(PostModel.DeleteRequest deleteRequest) {
        Post postEntity = postJpaRepository.findById(deleteRequest.postId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + deleteRequest.postId()));

        postJpaRepository.deleteById(postEntity.getId());
    }




    @Override
    public void findPostById(Long postId) {
        // rabbitMQ ::: 사용자 행동 패턴
        postEventHandler.handleUserBehaviorEvent("VIEWED", new PostUserBehaviorMessage(postId, RandomUtil.getRandomUserId()));

        // redis ::: 인기 게시글
        popularPostRedisService.incrementRealTimePopularPostCount(postId);

    }

    @Override
    public void addComment(Long postId) {
        // rabbitMQ ::: 사용자 행동 패턴
        postEventHandler.handleUserBehaviorEvent("COMMENT", new PostUserBehaviorMessage(postId, RandomUtil.getRandomUserId()));

        // redis ::: 인기 게시글
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }

    @Override
    public void likePost(Long postId) {
        // rabbitMQ ::: 사용자 행동 패턴
        postEventHandler.handleUserBehaviorEvent("LIKED", new PostUserBehaviorMessage(postId, RandomUtil.getRandomUserId()));

        // redis ::: 인기 게시글
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }
}
