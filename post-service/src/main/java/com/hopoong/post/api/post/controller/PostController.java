package com.hopoong.post.api.post.controller;

import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.service.PostService;
import com.hopoong.post.response.CommonResponseCodeEnum;
import com.hopoong.post.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 글 등록
    @PostMapping
    public SuccessResponse createPost(@RequestBody PostModel.CreateRequest request) {
        postService.createPost(request);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "Post successfully created");
    }

    // 글 조회
    @GetMapping("/{postId}")
    public SuccessResponse findPostById(@PathVariable Long postId) {
        postService.findPostById(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }

    // 댓글 등록
    @GetMapping("/posts/{postId}/comments")
    public SuccessResponse addComment(@PathVariable Long postId) {
        postService.addComment(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }

    // 좋아요
    @GetMapping("/posts/{postId}/like")
    public SuccessResponse likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }
}
