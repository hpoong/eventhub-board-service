package com.hopoong.post.api.post.repository;

import com.hopoong.post.domain.PostMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMetadataJpaRepository extends JpaRepository<PostMetadataEntity, Long> {


}
