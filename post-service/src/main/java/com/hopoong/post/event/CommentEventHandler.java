package com.hopoong.post.event;

import com.hopoong.core.model.PopularPostModel;
import com.hopoong.post.adapter.rabbitmq.RabbitmqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventHandler {

    private final RabbitmqProducer rabbitmqProducer;

    public void handleCommentEvent(PopularPostModel.CommentRabbitMQModel message) {
        rabbitmqProducer.sendCommentNotification(message);
    }

}
