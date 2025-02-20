package com.hopoong.post.api.popularpost.repository;

import com.hopoong.post.domain.PopularPost;
import com.hopoong.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularPostJpaRepository extends JpaRepository<PopularPost, Long> {

}
