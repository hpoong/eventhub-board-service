package com.hopoong.post.api.comment.repository;

import com.hopoong.post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
