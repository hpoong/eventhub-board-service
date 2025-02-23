package com.hopoong.post.api.comment.controller;

import com.hopoong.post.api.comment.service.CommentService;
import com.hopoong.post.response.CommonResponseCodeEnum;
import com.hopoong.post.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/{test}")
    public SuccessResponse test(@PathVariable String test) {
        commentService.test(test);
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, null);
    }



}
