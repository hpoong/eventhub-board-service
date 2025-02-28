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
public class RabbitMQUserBehaviorConfig {

    /*
     * 사용자 행동 패턴 Service
     */

    // Direct Exchange 설정
    @Bean
    public DirectExchange directUserBehaviorExchange() {
        return new DirectExchange(RabbitMQExchangeManager.BEHAVIOR);
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
    public Binding bindingCommentExchange(DirectExchange directUserBehaviorExchange, Queue postCommentsAggregationQueue) {
        return BindingBuilder.bind(postCommentsAggregationQueue)
                .to(directUserBehaviorExchange)
                .with("comments.behavior");
    }

    @Bean
    public Binding bindingLikedExchange(DirectExchange directUserBehaviorExchange, Queue postLikedAggregationQueue) {
        return BindingBuilder.bind(postLikedAggregationQueue)
                .to(directUserBehaviorExchange)
                .with("liked.behavior");
    }

    @Bean
    public Binding bindingViewedExchange(DirectExchange directUserBehaviorExchange, Queue postViewedAggregationQueue) {
        return BindingBuilder.bind(postViewedAggregationQueue)
                .to(directUserBehaviorExchange)
                .with("viewed.behavior");
    }

}
