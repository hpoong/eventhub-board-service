package com.hopoong.post.api.post.service;

import com.hopoong.core.model.popularpost.PostUserBehaviorMessage;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.api.post.model.PostEventModel;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.CommentJpaRepository;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.api.post.repository.PostMetadataJpaRepository;
import com.hopoong.post.domain.CommentEntity;
import com.hopoong.post.domain.PostEntity;
import com.hopoong.post.domain.PostMetadataEntity;
import com.hopoong.post.event.PostEventHandler;
import com.hopoong.post.util.RandomUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ApplicationEventPublisher eventPublisher;
    private final PostJpaRepository postJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final PostMetadataJpaRepository postMetadataJpaRepository;
    private final PostEventHandler postEventHandler;
    private final PopularPostRedisService popularPostRedisService;
    private final PostServiceImpl self;


    @Override
    @Transactional
    public void createPost(PostModel.CreateRequest createRequest) {
        PostEntity postEntity = PostEntity.createPost(
                createRequest.userId(),
                createRequest.title(),
                createRequest.content(),
                createRequest.categoryId(),
                RandomUtil.getRandomIntValue()
        );
        postJpaRepository.save(postEntity);

        // kafka ::: 포인트 등록
        eventPublisher.publishEvent(postEntity);
    }

    @Override
    @Transactional
    public void deletePost(PostModel.DeleteRequest deleteRequest) {
        PostEntity postEntity = findPostById(deleteRequest.postId());
        postJpaRepository.deleteById(postEntity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PostModel.InfoResponse fetchPost(Long postId) {
        PostEntity postEntity = findPostById(postId);
        return new PostModel.InfoResponse(postEntity.getId(), postEntity.getUserId(), postEntity.getTitle(), postEntity.getContent(), postEntity.getCategoryId(), postEntity.getViews());
    }

    @Override
    @Transactional
    public PostModel.InfoResponse findPostWithTracking(Long postId) {
        Long userId = RandomUtil.getRandomUserId();
        PostEntity postEntity = self.getPostByIdWithLock(postId);
        postEntity.setViews(postEntity.getViews() + 1);

        // 조회 후속 처리
        eventPublisher.publishEvent(new PostEventModel.UserInteractionEvent("VIEWED", postId, userId));
        return new PostModel.InfoResponse(postEntity.getId(), postEntity.getUserId(), postEntity.getTitle(), postEntity.getContent(), postEntity.getCategoryId(), postEntity.getViews());
    }

    @Override
    @Transactional
    public void addComment(Long postId, PostModel.CommentsCreateRequest request) {
        Long userId = RandomUtil.getRandomUserId();
        CommentEntity commentEntity = CommentEntity.createCommentEntity(
                postId,
                userId,
                request.content(),
                null
        );
        commentJpaRepository.save(commentEntity);

        // 댓글 후속 처리
        eventPublisher.publishEvent(new PostEventModel.UserInteractionEvent("COMMENT", commentEntity.getPostId(), commentEntity.getUserId()));
    }

    @Override
    @Transactional
    public void likePost(Long postId) {
        Long userId = RandomUtil.getRandomUserId();
        Optional<PostMetadataEntity> metadata = self.getPostMetadataByIdWithLock(postId);
        metadata.ifPresentOrElse(
                entity -> entity.setLikes(entity.getLikes() + 1),
                () -> {
                    PostMetadataEntity newMetadata = new PostMetadataEntity(postId, 1);
                    postMetadataJpaRepository.save(newMetadata);
                }
        );

        // 좋아요 후속 처리
        eventPublisher.publishEvent(new PostEventModel.UserInteractionEvent("LIKED", postId, userId));
    }

    /*
     * 비관적 락
     */
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

    private PostEntity findPostById(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: %s".formatted(postId)));
    }


    public void processUserInteraction(String type, Long postId, Long userId) {
        // rabbitMQ ::: 사용자 행동 패턴
        postEventHandler.handleUserBehaviorEvent(type, new PostUserBehaviorMessage(postId, userId));

        // redis ::: 인기 게시글
        popularPostRedisService.incrementRealTimePopularPostCount(postId);
    }

}
