package com.hopoong.post.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post { //  게시글

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


    // 게시글 생성 메서드 
    public static Post createPost(Long userId, String title, String content, Long categoryId, int views) {
        Post post = new Post();
        post.userId = userId;
        post.title = title;
        post.content = content;
        post.categoryId = categoryId;
        post.views = views;
        post.createdAt = LocalDateTime.now();
        post.updatedAt = LocalDateTime.now();
        return post;
    }

    // 게시글 업데이트 메서드 
    public void updatePost(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    // 조회수 증가 메서드 
    public void increaseViews() {
        this.views += 1;
    }

}
