package com.hopoong.post.api.post.model;

public class PostEventModel {

    public record UserInteractionEvent(String type, Long postId, Long userId) {}
}
