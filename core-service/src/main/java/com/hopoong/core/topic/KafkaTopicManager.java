package com.hopoong.core.topic;

public class KafkaTopicManager {



    // point 적립
    public static final String POST_POINT = "post-create-point-topic";
    public static final String POST_POINT_FAILED = "post-create-point-failed-topic";

    // point 적립 후 알람 발송
    public static final String POST_POINT_NOTIFICATION = "post-create-point-notification-topic";
    public static final String POST_POINT_NOTIFICATION_FAILED = "post-create-point-notification-failed-topic";

    // 알람 & 이메일 발송
    public static final String POST = "post-topic";
    public static final String LIKE = "like-topic";

    // 카테고리 별 인기 게시글 정보 저장
    public static final String BATCH_POPULAR_POSTS = "batch-popular-posts-topic";
}
