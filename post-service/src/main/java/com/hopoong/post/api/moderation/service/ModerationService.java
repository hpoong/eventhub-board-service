package com.hopoong.post.api.moderation.service;

import java.util.concurrent.CompletableFuture;

public interface ModerationService {
    CompletableFuture<Void> createModerationRequest(Long postId, boolean auto);
}
