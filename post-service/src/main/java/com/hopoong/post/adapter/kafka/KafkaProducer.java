package com.hopoong.post.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hopoong.core.model.notification.PostNotificationMessage;
import com.hopoong.core.model.popularpost.TrendingPostMessage;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    /*
     * 알람 / 이메일 publish ::: 글 등록
     */
    public void publishPostCreateEvent(PostNotificationMessage message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopicManager.POST, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }

    /*
     * 배치 publish ::: 카테고리 별 인기글 등록 ::: case 2 ::: 1000건씩 나누어 처리
     */
    public void publishPopularPostsEventIsBatch(List<TrendingPostMessage> message) {
        List<List<TrendingPostMessage>> partitions = Lists.partition(message, 1000);
        partitions.stream()
                .map(this::serializeToJson)
                .filter(serializedData -> serializedData != null)
                .forEach(this::sendToKafka);
    }


    /*
     * 배치 publish ::: 카테고리 별 인기글 등록 :: case 1 ::: 단건 뿌리기
     */
    public void publishPopularPostsEventIsNotBatch(List<TrendingPostMessage> message) {
        message.stream().forEach(data -> {
            try {
                kafkaTemplate.send(KafkaTopicManager.BATCH_POPULAR_POSTS, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }



    // 게시글 등록시 포인트 적립 publish
    public void publishPointUpdateEvent(PointUpdateMessage message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopicManager.POST_POINT, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }




    // **********

    private void sendToKafka(String data) {
        kafkaTemplate.send(KafkaTopicManager.BATCH_POPULAR_POSTS, UUID.randomUUID().toString(), data);
    }

    private String serializeToJson(List<TrendingPostMessage> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
