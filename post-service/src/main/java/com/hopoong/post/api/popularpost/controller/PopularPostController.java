package com.hopoong.post.api.popularpost.controller;

import com.hopoong.post.api.popularpost.service.PopularPostService;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.response.CommonResponseCodeEnum;
import com.hopoong.post.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/popular-post")
@RequiredArgsConstructor
public class PopularPostController {

    private final PopularPostService popularPostService;


    @GetMapping
    public SuccessResponse getPopularPostsFromCache() {
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, popularPostService.getPopularPostsFromCache());
    }


}
