package com.hopoong.email.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.PopularPostModel;
import com.hopoong.core.model.PostMessageModel;
import com.hopoong.core.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {


    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = KafkaTopic.BATCH_POPULAR_POSTS,
        groupId = "batch-popular-posts-topic-group",
        concurrency = "1"
    )
    public void consumePopularPostsNotification (ConsumerRecord<String, String> message) throws JsonProcessingException {
        List<PopularPostModel.TrendingPostModel> trendingPosts = objectMapper.readValue(
                message.value(),
                new TypeReference<List<PopularPostModel.TrendingPostModel>>() {}
        );
        log.info("📢 인기 개시글 알림: {}", trendingPosts);
    }

}
