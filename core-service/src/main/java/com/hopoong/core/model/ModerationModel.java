package com.hopoong.core.model;

public class ModerationModel {

    /*
     * RabbitMQ ::: 게시글 승인
     */
    public record ApprovalMessageModel(Long postId, Long userId, ApprovalStatus approvalStatus) {}


    public enum ApprovalStatus {
        AUTO_APPROVAL, AUTO_REJECTED, APPROVAL, REJECTED
    }
}

