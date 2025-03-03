package com.hopoong.core.model;

public class PostModel {

    /*
     * Kafka ::: 게시물 알림 (알림 & 이메일)
     */
    public record PostNotificationMessageModel(Long userId, String title, String content, Long categoryId) {}

}
