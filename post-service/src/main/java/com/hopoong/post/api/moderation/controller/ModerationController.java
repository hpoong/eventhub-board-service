package com.hopoong.post.api.moderation.controller;

import com.hopoong.post.api.moderation.service.ModerationService;
import com.hopoong.post.response.CommonResponseCodeEnum;
import com.hopoong.post.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/moderation")
@RequiredArgsConstructor
@EnableAsync
public class ModerationController {

    private final ModerationService moderationService;

    // 사용자가 검수 요청을 생성
    @PostMapping("/{postId}/request/auto-approve")
    public CompletableFuture<SuccessResponse> createAutoModerationRequest(@PathVariable Long postId) {
        return moderationService.createModerationRequest(postId, true)
                .thenApply(v -> new SuccessResponse(CommonResponseCodeEnum.SERVER, "검수 요청이 접수되었습니다."));
    }


    // 사용자가 검수 요청을 생성
    @PostMapping("/{postId}/request/auto-reject")
    public CompletableFuture<SuccessResponse> createModerationRequest(@PathVariable Long postId) {
        return moderationService.createModerationRequest(postId, false)
                .thenApply(v -> new SuccessResponse(CommonResponseCodeEnum.SERVER, "검수 요청이 접수되었습니다."));
    }

}
