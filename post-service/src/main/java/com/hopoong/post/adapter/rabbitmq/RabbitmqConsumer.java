package com.hopoong.post.adapter.rabbitmq;

import com.hopoong.core.topic.RabbitMQQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitmqConsumer {






    // 관리자 승인/검수 **********

    // 지연 시간이 지난 후 자동으로 승인
    @RabbitListener(queues = RabbitMQQueueManager.AUTO_APPROVAL_QUEUE)
    public void handleAutoApproval(String message) {
        System.out.println("자동 승인된 요청: " + message);
    }

    // 지연 시간이 지난 후 자동으로 거부
    @RabbitListener(queues = RabbitMQQueueManager.AUTO_REJECTED_QUEUE)
    public void handleAutoRejection(String message) {
        System.out.println("❌ 자동 거부된 요청: " + message);
    }

    // 관리자가 직접 승인
    @RabbitListener(queues = RabbitMQQueueManager.APPROVAL_QUEUE)
    public void handleManualApproval(String message) {
        System.out.println("관리자가 승인한 요청: " + message);
    }

    // 관리자가 직접 거부
    @RabbitListener(queues = RabbitMQQueueManager.REJECTED_QUEUE)
    public void handleManualRejection(String message) {
        System.out.println("❌ 관리자가 거부한 요청: " + message);
    }

}
