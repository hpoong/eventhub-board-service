package com.hopoong.core.model;

public record PostMessageModel(Long userId, String title, String content, Long categoryId) {}