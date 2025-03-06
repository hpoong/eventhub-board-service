package com.hopoong.core.model.post;

import java.time.LocalDateTime;

public record PointNotificationMessage(Long userId, Long postId, Long earnedPoints, Long totalPoint, LocalDateTime timestamp, String eventType) {}