package com.hopoong.post.config;

import com.hopoong.core.topic.RabbitMQExchangeManager;
import com.hopoong.core.topic.RabbitMQQueueManager;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopologyConfig {

    // Direct Exchange 설정
    @Bean
    public DirectExchange directCommentExchange() {
        return new DirectExchange(RabbitMQExchangeManager.COMMENT);
    }

    @Bean
    public DirectExchange directLikedExchange() {
        return new DirectExchange(RabbitMQExchangeManager.LIKED);
    }

    @Bean
    public DirectExchange directViewedExchange() {
        return new DirectExchange(RabbitMQExchangeManager.VIEWED);
    }

}
