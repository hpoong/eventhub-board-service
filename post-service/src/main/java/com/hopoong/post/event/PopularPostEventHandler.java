package com.hopoong.post.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PopularPostEventHandler {

    private final KafkaProducer kafkaProducer;

    public void handleFetchPopularPostsEvent(List<PopularPostModel.TrendingPostModel> message) {
        kafkaProducer.publishPopularPostsEvent(message);
    }
}
