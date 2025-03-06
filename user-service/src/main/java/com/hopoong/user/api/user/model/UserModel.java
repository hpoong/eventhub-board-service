package com.hopoong.user.api.user.model;

import com.hopoong.user.domain.UserEntity;

public class UserModel {


    public record UserInfoResponse(Long id, String userName, String email, Long point) {
        public static UserInfoResponse from(UserEntity userEntity) {
            return new UserInfoResponse(
                    userEntity.getId(),
                    userEntity.getUserName(),
                    userEntity.getEmail(),
                    userEntity.getPoint()
            );
        }
    }

    public record UserPointUpdateRequest(Long point) {}


    public record UserSignupRequest(String userName, String email) {}

}
