package com.hopoong.post.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.api.post.service.PostService;
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
    private final PostService postService;

    @KafkaListener(
            topics = KafkaTopicManager.POST_POINT_FAILED,
            groupId = "post-create-point-failed-group",
            concurrency = "3"
    ) //  포인트 적립 실패시
    public void publishRollbackPointUpdateEvent(ConsumerRecord<String, String> message) throws JsonProcessingException {
        PointUpdateMessage pointUpdateMessage = objectMapper.readValue(message.value(), PointUpdateMessage.class);
        postService.deletePost(new PostModel.DeleteRequest(pointUpdateMessage.userId(), pointUpdateMessage.postId()));
    }

}
