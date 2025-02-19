package com.hopoong.post.api.post.model;

public class PostModel {

    public record CreateRequest(Long userId, String title, String content, Long categoryId) {}


    public record UpdateRequest(Long id, String title, String content, Long categoryId) {
        public static UpdateRequest withId(Long id, UpdateRequest request) {
            return new UpdateRequest(id, request.title(), request.content(), request.categoryId());
        }
    }

}