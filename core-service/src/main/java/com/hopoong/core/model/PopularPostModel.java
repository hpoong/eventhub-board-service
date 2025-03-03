package com.hopoong.core.model;

public class PopularPostModel {

    /*
     * Kafka ::: 카테고리 별 인기 게시물
     */
    public record TrendingPostMessageModel(Long id, Long userId, String title, String content, Long categoryId, Integer views, Long rn) {}

    /*
     * RabbitMQ ::: 사용자 행동패턴 분석
     */
    public record PostUserBehaviorMessageModel(Long postId, Long userId) {}
}
