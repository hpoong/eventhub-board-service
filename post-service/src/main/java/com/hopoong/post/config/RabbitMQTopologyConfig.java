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

    // Durable 큐 설정 (메시지 유실 방지)
    @Bean
    public Queue postCommentsAggregationQueue() {
        return new Queue(RabbitMQQueueManager.COMMENT, true);
    }

    @Bean
    public Queue postLikedAggregationQueue() {
        return new Queue(RabbitMQQueueManager.LIKED, true);
    }

    @Bean
    public Queue postViewedAggregationQueue() {
        return new Queue(RabbitMQQueueManager.VIEWED, true);
    }

    // Exchange <-> Queue 바인딩 (DirectExchange + Routing Key 설정)
    @Bean
    public Binding bindingCommentExchange(DirectExchange directCommentExchange, Queue postCommentsAggregationQueue) {
        return BindingBuilder.bind(postCommentsAggregationQueue)
                .to(directCommentExchange)
                .with("aggregation.count");
    }

    @Bean
    public Binding bindingLikedExchange(DirectExchange directLikedExchange, Queue postLikedAggregationQueue) {
        return BindingBuilder.bind(postLikedAggregationQueue)
                .to(directLikedExchange)
                .with("aggregation.count");
    }

    @Bean
    public Binding bindingViewedExchange(DirectExchange directViewedExchange, Queue postViewedAggregationQueue) {
        return BindingBuilder.bind(postViewedAggregationQueue)
                .to(directViewedExchange)
                .with("aggregation.count");
    }

}
