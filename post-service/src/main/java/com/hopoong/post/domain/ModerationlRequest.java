package com.hopoong.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "moderation_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModerationlRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContentType contentType; // POST, COMMENT, TRANSLATION

    @Column(nullable = false)
    private Long contentId; // 게시글 또는 댓글 ID

    @Column(nullable = false)
    private Long userId; // 요청한 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModerationStatus status = ModerationStatus.PENDING; // 기본값 PENDING

    @Column(columnDefinition = "TEXT")
    private String reason; // 거절 사유 (nullable)

    private Long reviewedBy; // 승인/거절한 관리자 ID (nullable)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}


enum ContentType {
    POST, COMMENT, TRANSLATION
}

enum ModerationStatus {
    PENDING, APPROVED, REJECTED
}