package com.hopoong.post.api.post.service;

import com.hopoong.post.api.post.model.PostEventModel;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.repository.CommentJpaRepository;
import com.hopoong.post.api.post.repository.PostJpaRepository;
import com.hopoong.post.api.post.repository.PostMetadataJpaRepository;
import com.hopoong.post.domain.CommentEntity;
import com.hopoong.post.domain.PostEntity;
import com.hopoong.post.domain.PostMetadataEntity;
import com.hopoong.post.util.RandomUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final PostLockService postLockService;


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
        PostEntity postEntity = postLockService.getPostByIdWithLock(postId);
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
        Optional<PostMetadataEntity> metadata = postLockService.getPostMetadataByIdWithLock(postId);
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

    private PostEntity findPostById(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: %s".formatted(postId)));
    }

}
