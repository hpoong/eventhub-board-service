package com.hopoong.email.app.popularpost.repository;

import com.hopoong.email.domain.PopularPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularPostJpaRepository extends JpaRepository<PopularPostEntity, Long> {

}
