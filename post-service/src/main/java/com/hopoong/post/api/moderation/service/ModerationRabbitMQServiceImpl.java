package com.hopoong.post.api.moderation.service;


import com.hopoong.core.model.ModerationModel;
import com.hopoong.core.topic.RabbitMQExchangeManager;
import com.hopoong.core.topic.RabbitMQQueueManager;
import com.hopoong.post.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ModerationRabbitMQServiceImpl implements ModerationRabbitMQService {

    private final AmqpAdmin amqpAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final int xDelayMs = 180000; // 3분

    @Override
    public void createModerationRequest(ModerationModel.ApprovalMessageModel messageModel) {
        System.out.println(TimeUtil.getFormattedTimestamp());
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", xDelayMs); // 지연 시간 설정

        // 지연 큐로 메시지 전송
        rabbitTemplate.convertAndSend(
                RabbitMQExchangeManager.MODERATION_APPROVAL,
                RabbitMQQueueManager.DELAYED_QUEUE,
                messageModel, // data
                message -> {
                    message.getMessageProperties().getHeaders().putAll(headers);
                    return message;
                }
        );
    }

}
