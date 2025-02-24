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

    @PostMapping
    public SuccessResponse createPost(@RequestBody PostModel.CreateRequest request) {
        postService.createPost(request);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "Post successfully created");
    }

    @GetMapping("/{postId}")
    public SuccessResponse findPostById(@PathVariable Long postId) {
        postService.findPostById(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }

    @GetMapping("/posts/{postId}/comments")
    public SuccessResponse addComment(@PathVariable Long postId) {
        postService.addComment(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }


    @GetMapping("/posts/{postId}/like")
    public SuccessResponse likePost(@PathVariable Long postId) {
        postService.likePost(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }
}
