package com.hopoong.email.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.popularpost.TrendingPostMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



//consumePopularPostsNotification
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;



    // case 2 ::: 1000건씩 나누어 처리
    @KafkaListener(
            topics = KafkaTopicManager.BATCH_POPULAR_POSTS,
            groupId = "is-batch",
            containerFactory = "kafkaBatchListenerContainerFactory",
            concurrency = "3"
    )
    public void isBatch(List<ConsumerRecord<String, String>> messages) throws JsonProcessingException {
        List<TrendingPostMessage> allTrendingPosts = new ArrayList<>();

        for (ConsumerRecord<String, String> message : messages) {
            List<TrendingPostMessage> trendingPosts = objectMapper.readValue(
                    message.value(),
                    new TypeReference<List<TrendingPostMessage>>() {}
            );
            allTrendingPosts.addAll(trendingPosts);
        }

        log.info("인기 게시글 알림 (배치 처리): {}개 메시지, 총 {}개 데이터", messages.size(), allTrendingPosts.size());
    }


    // case 1 ::: 단건 뿌리기
    @KafkaListener(
            topics = KafkaTopicManager.BATCH_POPULAR_POSTS,
            groupId = "is-not-batch",
            containerFactory = "kafkaBatchListenerContainerFactory",
            concurrency = "1"
    )
    public void isNotBatch(List<ConsumerRecord<String, String>> messages) throws JsonProcessingException {

        List<TrendingPostMessage> allTrendingPosts = new ArrayList<>();

        for (ConsumerRecord<String, String> message : messages) {
            TrendingPostMessage trendingPosts = objectMapper.readValue(
                    message.value(),
                    TrendingPostMessage.class
            );
            allTrendingPosts.add(trendingPosts);
        }

        log.info("인기 게시글 알림 (배치 처리): {}개 메시지, 총 {}개 데이터", messages.size(), allTrendingPosts.size());
    }


}
