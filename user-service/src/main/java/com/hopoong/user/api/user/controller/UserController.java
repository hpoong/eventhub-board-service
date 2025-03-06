package com.hopoong.user.api.user.controller;

import com.hopoong.core.response.CommonResponseCodeEnum;
import com.hopoong.core.response.SuccessResponse;
import com.hopoong.user.api.user.model.UserModel;
import com.hopoong.user.api.user.service.UserService;
import com.hopoong.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@EnableAsync
public class UserController {

    private final UserService userService;


    /*
     * 사용자 조회
     */
    @GetMapping("/{userId}")
    public SuccessResponse getUserById(@PathVariable Long userId) {
        UserEntity userEntity = userService.getUserById(userId);
        return new SuccessResponse(CommonResponseCodeEnum.USER, UserModel.UserInfoResponse.from(userEntity));
    }


    /*
     * 사용자 등록
     */
    @PostMapping("/signup")
    public SuccessResponse signup(@RequestBody UserModel.UserSignupRequest requestModel) {
        userService.signup(requestModel);
        return new SuccessResponse(CommonResponseCodeEnum.USER, null);
    }

}
