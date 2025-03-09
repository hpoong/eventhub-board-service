package com.hopoong.post.api.post.model;

public class PostModel {

    public record CreateRequest(Long userId, String title, String content, Long categoryId) {}

    public record DeleteRequest(Long userId, Long postId) {}

}