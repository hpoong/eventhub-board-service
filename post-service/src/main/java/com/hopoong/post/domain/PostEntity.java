package com.hopoong.post.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity { //  게시글

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Integer views = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;





    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTagEntity> postTagEntities = new HashSet<>();



    // 게시글 생성 메서드
    public static PostEntity createPost(Long userId, String title, String content, Long categoryId, int views) {
        PostEntity postEntity = new PostEntity();
        postEntity.userId = userId;
        postEntity.title = title;
        postEntity.content = content;
        postEntity.categoryId = categoryId;
        postEntity.views = views;
        postEntity.createdAt = LocalDateTime.now();
        postEntity.updatedAt = LocalDateTime.now();
        return postEntity;
    }

}
