package com.hopoong.notification.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.PostModel;
import com.hopoong.core.topic.KafkaTopicManager;
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


    @KafkaListener(
        topics = KafkaTopicManager.LIKE,
        groupId = "like-topic-notification-group",
        concurrency = "3"
    )
    public void consumeLikeNotification(ConsumerRecord<String, String> message) {
        log.info("👍 좋아요 알림: {}", message);
    }


    @KafkaListener(
        topics = KafkaTopicManager.POST,
        groupId = "post-topic-notification-group",
        concurrency = "3"
    )
    public void consumePostNotification(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PostModel.PostNotificationMessageModel postNotificationMessageModel = objectMapper.readValue(message.value(), PostModel.PostNotificationMessageModel.class);
        log.info("👍 게시글 알림: {}", postNotificationMessageModel);
    }

}
