package com.hopoong.core.topic;

public class RabbitMQExchangeManager {

    // 사용자 행동 패턴 Direct Exchange
    public static final String BEHAVIOR = "post.user.behavior.exchange";

    // 관리자 승인/검수 Direct Exchange
    public static final String MODERATION_APPROVAL = "post.moderation.approval.exchange";
}
