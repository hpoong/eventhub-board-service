package com.hopoong.post.api.popularpost.controller;

import com.hopoong.post.api.popularpost.service.PopularPostService;
import com.hopoong.post.response.CommonResponseCodeEnum;
import com.hopoong.post.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/popular-post")
@RequiredArgsConstructor
public class PopularPostController {

    private final PopularPostService popularPostService;


    /*
     * 실시간 인기글 조회
     */
    @GetMapping("/realtime")
    public SuccessResponse getRealTimeTopPopularPosts() {
        return new SuccessResponse(CommonResponseCodeEnum.SERVER, popularPostService.getRealTimeTopPopularPosts(10));
    }


}
