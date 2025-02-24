package com.hopoong.post.adapter.rabbitmq;

import com.hopoong.core.model.PopularPostModel;
import com.hopoong.core.topic.RabbitMQExchangeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendCommentNotification(PopularPostModel.CommentRabbitMQModel message) {
        rabbitTemplate.convertAndSend(RabbitMQExchangeManager.COMMENT, "", message);
    }

}
