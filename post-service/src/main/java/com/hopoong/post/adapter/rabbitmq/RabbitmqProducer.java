package com.hopoong.post.adapter.rabbitmq;

import com.hopoong.core.topic.RabbitmqTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendCommentNotification(String message) {
        rabbitTemplate.convertAndSend(RabbitmqTopic.COMMENT, "", message);
    }

}
