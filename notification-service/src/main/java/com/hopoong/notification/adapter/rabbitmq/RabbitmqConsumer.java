package com.hopoong.notification.adapter.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqConsumer {

    @RabbitListener(queues = "comment.notification-1")
    public void receiveNotificationUser1(String message) {
        System.out.println("📩 [comment.notification] 알림 받음: " + message);
    }

}
