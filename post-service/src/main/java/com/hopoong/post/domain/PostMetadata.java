package com.hopoong.post.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "post_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMetadata { // 게시글 부가 정보

    @Id
    private Long postId;

    @Column(nullable = false)
    private Integer likes = 0;

    @Column(nullable = false)
    private Boolean translated = false;

    @Column(columnDefinition = "TEXT")
    private String translatedContent;

    @Column(nullable = false)
    private Boolean archived = false;

    private LocalDateTime lastViewedAt;
}