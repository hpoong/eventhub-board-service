package com.hopoong.core.topic;

public class RabbitMQQueueManager {


    // 사용자 행동 패턴 Queue
    public static final String COMMENT = "post.comments.behavior.queue";
    public static final String LIKED = "post.liked.behavior.queue";
    public static final String VIEWED = "post.viewed.behavior.queue";


    // 관리자 승인/검수 Queue
    public static final String DELAYED_QUEUE = "post.delayed.queue";
    public static final String APPROVAL_QUEUE = "post.approval.queue";
    public static final String REJECTED_QUEUE = "post.rejected.queue";
    public static final String AUTO_APPROVAL_QUEUE = "post.auto.approval.queue";
    public static final String AUTO_REJECTED_QUEUE = "post.auto.rejected.queue";
}
