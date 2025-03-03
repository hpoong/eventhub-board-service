package com.hopoong.post.config;

import com.hopoong.core.topic.RabbitMQExchangeManager;
import com.hopoong.core.topic.RabbitMQQueueManager;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQModerationApprovalConfig {

    /*
     * 관리자 승인/검수 Service
     */

    // Delayed Exchange (지연 메시지를 지원하는 Exchange)
    @Bean
    public CustomExchange delayedExchange() {
        return new CustomExchange(RabbitMQExchangeManager.MODERATION_APPROVAL, "x-delayed-message", true, false,
                Map.of("x-delayed-type", "direct"));
    }

    // 지연 큐 (일정 시간 후 메시지가 이동)
    @Bean
    public Queue delayedQueue() {
        return new Queue(RabbitMQQueueManager.DELAYED_QUEUE, true);
    }

    @Bean
    public Queue approvalQueue() {
        return new Queue(RabbitMQQueueManager.APPROVAL_QUEUE, true);
    }

    @Bean
    public Queue rejectedQueue() {
        return new Queue(RabbitMQQueueManager.REJECTED_QUEUE, true);
    }

    @Bean
    public Queue autoApprovalQueue() {
        return new Queue(RabbitMQQueueManager.AUTO_APPROVAL_QUEUE, true);
    }

    @Bean
    public Queue autoRejectedQueue() {
        return new Queue(RabbitMQQueueManager.AUTO_REJECTED_QUEUE, true);
    }

    // Binding (Exchange와 Queue 연결)
    @Bean
    public Binding delayedQueueBinding() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(RabbitMQQueueManager.DELAYED_QUEUE).noargs();
    }

    @Bean
    public Binding approvalQueueBinding() {
        return BindingBuilder.bind(approvalQueue()).to(delayedExchange()).with(RabbitMQQueueManager.APPROVAL_QUEUE).noargs();
    }

    @Bean
    public Binding rejectedQueueBinding() {
        return BindingBuilder.bind(rejectedQueue()).to(delayedExchange()).with(RabbitMQQueueManager.REJECTED_QUEUE).noargs();
    }

    @Bean
    public Binding autoApprovalQueueBinding() {
        return BindingBuilder.bind(autoApprovalQueue()).to(delayedExchange()).with(RabbitMQQueueManager.AUTO_APPROVAL_QUEUE).noargs();
    }

    @Bean
    public Binding autoRejectedQueueBinding() {
        return BindingBuilder.bind(autoRejectedQueue()).to(delayedExchange()).with(RabbitMQQueueManager.AUTO_REJECTED_QUEUE).noargs();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
