package com.hopoong.user.api.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.user.api.user.model.UserModel;
import com.hopoong.user.domain.UserEntity;

public interface UserService {

    UserEntity getUserById(Long userId);
    UserEntity addUserPoints(Long userId, UserModel.UserPointUpdateRequest points);
    void signup(UserModel.UserSignupRequest requestModel);
    void updatePointWithNotification(PointUpdateMessage pointUpdateMessage) throws JsonProcessingException;
}
