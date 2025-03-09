package com.hopoong.core.model.popularpost;

/*
 * RabbitMQ ::: 사용자 행동패턴 분석
 */
public record PostUserBehaviorMessage(Long postId, Long userId) {}