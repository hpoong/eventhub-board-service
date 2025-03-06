package com.hopoong.post.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.core.topic.KafkaTopicManager;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    public void publishPostCreateEvent(PostModel.CreateRequest message) throws JsonProcessingException {
        kafkaTemplate.send(KafkaTopicManager.POST, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }

    /*
     * 배치 publish ::: 카테고리 별 인기글 등록
     */
    public void publishPopularPostsEvent(List<PopularPostModel.TrendingPostModel> message) {


        // case 2 ::: 1000건씩 나누어 처리
        List<List<PopularPostModel.TrendingPostModel>> partitions = Lists.partition(message, 1000);
        partitions.stream().forEach(data -> {
            try {
                kafkaTemplate.send(KafkaTopicManager.BATCH_POPULAR_POSTS, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(data));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });


//        // case 1 ::: 단건씩 뿌리기
//        message.stream().forEach(data -> {
//            try {
//                kafkaTemplate.send(KafkaTopic.BATCH_POPULAR_POSTS, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(data));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });



    }

    // 게시글 등록시 포인트 적립
    public void publishPointUpdateEvent(Post event) throws JsonProcessingException {
        PointUpdateMessage message = new PointUpdateMessage(event.getUserId(), event.getId(), 1000L, LocalDateTime.now(), "post_created");
        kafkaTemplate.send(KafkaTopicManager.POST_POINT, String.valueOf(UUID.randomUUID().toString()), objectMapper.writeValueAsString(message));
    }

}
