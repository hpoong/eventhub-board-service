package com.hopoong.user.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.post.PointNotificationMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    // 알람 처리
    public void publishPointUpdateNotificationEvent(PointNotificationMessage message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopicManager.POST_POINT_NOTIFICATION, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }

}
