package com.hopoong.user.api.user.repository;

import com.hopoong.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityJpaRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);
}
