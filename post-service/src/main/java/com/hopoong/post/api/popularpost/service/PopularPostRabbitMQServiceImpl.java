package com.hopoong.post.api.popularpost.service;

import com.hopoong.core.model.PopularPostModel;
import com.hopoong.core.topic.RabbitMQQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PopularPostRabbitMQServiceImpl implements PopularPostRabbitMQService {

    private final RabbitTemplate rabbitTemplate;
    private static final int BATCH_SIZE = 100; // 100개씩 처리

    @Override
    public void aggregatePostMetrics() {
        processQueue(RabbitMQQueueManager.COMMENT);
        processQueue(RabbitMQQueueManager.LIKED);
        processQueue(RabbitMQQueueManager.VIEWED);
    }


    private void processQueue(String queueName) {
        System.out.println("▶ 큐 소비 시작: " + queueName);

        int totalProcessed = 0;
        while (true) {
            int count = 0;
            while (count < BATCH_SIZE) { // BATCH_SIZE 개씩 처리
                PopularPostModel.PostUserBehaviorMessageModel message = (PopularPostModel.PostUserBehaviorMessageModel) rabbitTemplate.receiveAndConvert(queueName);
                if (message == null) {
                    break; // 메시지가 없으면 현재 배치 종료
                }
                System.out.println("▶ 소비된 메시지 [" + queueName + "]: " + message);

                // TODO : Service 로직

                count++;
            }

            totalProcessed += count;

            // 만약 BATCH_SIZE보다 적게 가져왔다면 모든 메시지 소비 완료 → 종료
            if (count < BATCH_SIZE) {
                break;
            }
        }

        System.out.println("▶ 큐 소비 완료: " + queueName + " (총 " + totalProcessed + "개 처리)");
    }


}
