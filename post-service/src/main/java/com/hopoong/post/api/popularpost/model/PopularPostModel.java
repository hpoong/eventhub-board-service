package com.hopoong.post.api.popularpost.model;

public class PopularPostModel {

    public record TrendingPostModel(Long id, Long userId, String title, String content, Long categoryId, Integer views, Long rn) {}
}
