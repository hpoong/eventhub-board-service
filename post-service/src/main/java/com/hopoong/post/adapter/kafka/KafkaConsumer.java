package com.hopoong.post.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.PostModel;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;


    @KafkaListener(
            topics = KafkaTopicManager.POST_POINT_FAILED,
            groupId = "post-create-point-failed-group",
            concurrency = "3"
    ) //  포인트 적립 실패시
    public void publishRollbackPointUpdateEvent(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PointUpdateMessage pointUpdateMessage = objectMapper.readValue(message.value(), PointUpdateMessage.class);
        log.info("포인트 적립 실패시 ::: {}", pointUpdateMessage);
    }

}
