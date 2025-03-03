package com.hopoong.post.api.moderation.service;

import com.hopoong.core.model.ModerationModel;

public interface ModerationRabbitMQService {

    void createModerationRequest(ModerationModel.ApprovalMessageModel autoApproval);

}




