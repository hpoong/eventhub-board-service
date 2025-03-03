package com.hopoong.post.api.moderation.service;

import com.hopoong.core.model.ModerationModel;
import com.hopoong.post.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {


    private final ModerationRabbitMQService moderationRabbitMQService;

    @Async
    @Override
    public CompletableFuture<Void> createModerationRequest(Long postId, boolean auto) {
        return CompletableFuture.runAsync(() -> {
            moderationRabbitMQService.createModerationRequest(
                    new ModerationModel.ApprovalMessageModel(
                            postId,
                            RandomUtil.getRandomUserId(),
                            auto ? ModerationModel.ApprovalStatus.AUTO_APPROVAL : ModerationModel.ApprovalStatus.AUTO_REJECTED
                    )
            );
            System.out.println("검수 요청을 처리 중... postId: " + postId);
        });
    }

}
