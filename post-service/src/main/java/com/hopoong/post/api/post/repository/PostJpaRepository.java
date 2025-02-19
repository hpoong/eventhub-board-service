package com.hopoong.post.api.post.repository;

import com.hopoong.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

}
