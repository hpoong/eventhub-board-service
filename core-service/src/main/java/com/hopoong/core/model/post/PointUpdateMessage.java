package com.hopoong.core.model.post;

import java.time.LocalDateTime;

public record PointUpdateMessage(Long userId, Long postId, Long point, LocalDateTime timestamp, String eventType) {}