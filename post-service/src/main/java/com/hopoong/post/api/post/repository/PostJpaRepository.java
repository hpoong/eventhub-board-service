package com.hopoong.post.api.post.repository;

import com.hopoong.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostJpaRepository extends JpaRepository<Post, Long> {


    @Query(value = """
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY category_id ORDER BY views DESC) AS rn
            FROM posts
        ) ranked
        WHERE ranked.rn <= 100
        """, nativeQuery = true)
    List<Object[]> findTop100ByOrderByViewsDesc();
}
