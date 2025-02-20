package com.hopoong.post.api.post.repository;

import com.hopoong.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findTop100ByOrderByViewsDesc();
}
