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

    /*
     * 사용자 행동 패턴 publish ::: 댓글 / 좋아요 / 조회
     */
    public void publishPostUserBehaviorEvent(String type, PopularPostModel.PostUserBehaviorMessageModel message) {
        switch (type) {
            case "COMMENT" -> rabbitTemplate.convertAndSend(RabbitMQExchangeManager.BEHAVIOR, "comments.behavior", message);
            case "LIKED" -> rabbitTemplate.convertAndSend(RabbitMQExchangeManager.BEHAVIOR, "liked.behavior", message);
            case "VIEWED" -> rabbitTemplate.convertAndSend(RabbitMQExchangeManager.BEHAVIOR, "viewed.behavior", message);
        }
    }

}
