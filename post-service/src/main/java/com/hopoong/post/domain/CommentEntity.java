package com.hopoong.post.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity { // 댓글 정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private Long parentCommentId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();




    public static CommentEntity createCommentEntity(Long postId, Long userId, String content, Long parentCommentId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.postId = postId;
        commentEntity.userId = userId;
        commentEntity.content = content;
        commentEntity.parentCommentId = (parentCommentId != null) ? parentCommentId : 0L;
        commentEntity.createdAt = LocalDateTime.now();
        commentEntity.updatedAt = LocalDateTime.now();
        return commentEntity;
    }

}