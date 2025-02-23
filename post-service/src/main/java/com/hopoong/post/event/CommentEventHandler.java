package com.hopoong.post.event;

import com.hopoong.post.adapter.rabbitmq.RabbitmqProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentEventHandler {

    private final RabbitmqProducer rabbitmqProducer;

    public void handleCommentEvent(String message) {
        rabbitmqProducer.sendCommentNotification(message);
    }

}
