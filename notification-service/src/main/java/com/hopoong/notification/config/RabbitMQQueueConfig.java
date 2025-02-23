package com.hopoong.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQQueueConfig {

    @Bean
    public Queue commentNotificationQueue1() {
        return new Queue("comment.notification-1", false);
    }

    @Bean
    public Binding bindingCommentNotification1(FanoutExchange fanoutExchange, Queue commentNotificationQueue1) {
        return BindingBuilder.bind(commentNotificationQueue1).to(fanoutExchange);
    }


    @Bean
    public Queue commentNotificationQueue2() {
        return new Queue("comment.notification-2", false);
    }

    @Bean
    public Binding bindingCommentNotification2(FanoutExchange fanoutExchange, Queue commentNotificationQueue2) {
        return BindingBuilder.bind(commentNotificationQueue2).to(fanoutExchange);
    }

}
