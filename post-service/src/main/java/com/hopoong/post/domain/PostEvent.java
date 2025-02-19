package com.hopoong.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEvent { // 게시글 이벤트 로그

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventType eventType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}

enum EventType {
    CREATE, UPDATE, DELETE
}
