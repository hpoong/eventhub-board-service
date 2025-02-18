package com.hopoong.eventhubboard.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "popular_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularPost { // 인기 게시글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private Integer views;

    @Column(nullable = false)
    private Integer rank;

    @Column(nullable = false, updatable = false)
    private LocalDateTime cachedAt = LocalDateTime.now();
}
