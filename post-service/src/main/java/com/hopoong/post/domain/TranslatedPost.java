package com.hopoong.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "translated_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslatedPost { // 번역된 게시글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, length = 10)
    private String language;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String translatedContent;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
