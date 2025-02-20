package com.hopoong.email.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.PostMessageModel;
import com.hopoong.core.topic.KafkaTopic;
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
        topics = KafkaTopic.COMMENT,
        groupId = "comment-topic-email-group",
        concurrency = "1"
    )
    public void consumeCommentNotification (ConsumerRecord<String, String> message) throws JsonProcessingException {
        log.info("🔥 댓글 알림: {}", message);
    }

    @KafkaListener(
            topics = KafkaTopic.LIKE,
            groupId = "like-topic-email-group",
            concurrency = "1"
    )
    public void consumeLikeNotification(ConsumerRecord<String, String> message) throws JsonProcessingException {
        log.info("🔥 좋아요 알림: {}", message);
    }

    @KafkaListener(
            topics = KafkaTopic.POST,
            groupId = "post-topic-email-group",
            concurrency = "1"
    )
    public void consumePostNotification(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PostMessageModel postMessageModel = objectMapper.readValue(message.value(), PostMessageModel.class);
        log.info("🔥 게시글 알림: {}", postMessageModel);
    }

}
