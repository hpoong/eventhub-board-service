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
        postService.create(request);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "Post successfully created");
    }

    @PutMapping("/{postId}")
    public SuccessResponse updatePost(@PathVariable Long postId, @RequestBody PostModel.UpdateRequest request) {
        postService.update(PostModel.UpdateRequest.withId(postId, request));
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "Post successfully updated");
    }

    @DeleteMapping("/{postId}")
    public SuccessResponse deletePost(@PathVariable Long postId) {
        postService.delete(postId);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, "Post successfully deleted");
    }

}
