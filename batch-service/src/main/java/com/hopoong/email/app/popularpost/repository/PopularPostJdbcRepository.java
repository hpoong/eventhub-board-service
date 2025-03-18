package com.hopoong.email.app.popularpost.repository;

import com.hopoong.email.domain.PopularPostEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PopularPostJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public PopularPostJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void batchInsert(List<PopularPostEntity> popularPostEntities) {
        String sql = "INSERT INTO popular_posts (post_id, views, post_rank, retrieved_at, cached_at) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, popularPostEntities, 100, (ps, entity) -> {
            ps.setLong(1, entity.getPostId());
            ps.setInt(2, entity.getViews());
            ps.setInt(3, entity.getPostRank());
            ps.setString(4, entity.getRetrievedAt());
            ps.setTimestamp(5, Timestamp.valueOf(entity.getCachedAt()));
        });
    }
}
