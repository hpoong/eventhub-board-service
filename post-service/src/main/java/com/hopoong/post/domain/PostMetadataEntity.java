package com.hopoong.post.domain;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "post_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMetadataEntity { // 게시글 부가 정보

    @Id
    private Long postId;

    @Column(nullable = false)
    private Integer likes = 0;



}