package com.hopoong.user.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.post.PointNotificationMessage;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import com.hopoong.user.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;
    private final UserService userService;



    @KafkaListener(
            topics = KafkaTopicManager.POST_POINT_FAILED,
            groupId = "post-create-point-failed-group",
            concurrency = "3"
    ) //  알람 발송 실패 처리
    public void publishRollbackPointNotificationEvent(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PointNotificationMessage pointNotificationMessage = objectMapper.readValue(message.value(), PointNotificationMessage.class);
        log.info("알람 발송 실패 Consumer ::: {}", pointNotificationMessage);
    }


    @KafkaListener(
            topics = KafkaTopicManager.POST_POINT,
            groupId = "post-create-point-group",
            concurrency = "3"
    ) //  포인트 적립
    public void publishRollbackPointUpdateEvent(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PointUpdateMessage pointUpdateMessage = objectMapper.readValue(message.value(), PointUpdateMessage.class);
        userService.updatePointWithNotification(pointUpdateMessage);
        log.info("포인트 적립 Consumer ::: {}", pointUpdateMessage);
    }

}
