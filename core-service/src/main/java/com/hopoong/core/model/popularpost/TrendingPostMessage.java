package com.hopoong.core.model.popularpost;

/*
 * Kafka ::: 카테고리 별 인기 게시물
 */
public record TrendingPostMessage(Long id, Long userId, String title, String content, Long categoryId, Integer views, Long rn) {}