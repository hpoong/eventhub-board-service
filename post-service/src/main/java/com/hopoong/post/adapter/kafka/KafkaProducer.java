package com.hopoong.post.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.topic.KafkaTopic;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.post.model.PostModel;
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

    public void publishPostEvent(PostModel.CreateRequest message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopic.POST, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }

    public void publishPopularPostsEvent(List<PopularPostModel.TrendingPostModel> message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopic.BATCH_POPULAR_POSTS, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }
}
