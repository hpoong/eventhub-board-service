package com.hopoong.user.app.behavior;


import com.hopoong.core.topic.RabbitMQQueueManager;
import com.hopoong.user.adapter.rabbitmq.RabbitmqConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BehaviorServiceImpl implements BehaviorService {

    private final RabbitmqConsumer rabbitmqConsumer;


    /*
     * 1시간 마다 사용자 행동 패턴 게시글 집계 처리
     */
    @Override
    public void aggregateUserPostBehaviorMetrics() {
        List<String> queues = List.of(
                RabbitMQQueueManager.COMMENT,
                RabbitMQQueueManager.LIKED,
                RabbitMQQueueManager.VIEWED
        );

        queues.forEach(rabbitmqConsumer::processQueue);
    }


}
