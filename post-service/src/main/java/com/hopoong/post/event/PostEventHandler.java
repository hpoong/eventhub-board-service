package com.hopoong.post.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.core.model.PopularPostModel;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.adapter.rabbitmq.RabbitmqProducer;
import com.hopoong.post.api.post.model.PostModel;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostEventHandler {

    private final KafkaProducer kafkaProducer;
    private final RabbitmqProducer rabbitmqProducer;


//    /*
//     * 글 등록시 이벤트
//     */
//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void handlePostCreateEvent(PostModel.CreateRequest event) throws JsonProcessingException {
//
//        // kafka 처리
//        kafkaProducer.publishPostCreateEvent(event);
//    }


    /*
     * 글 등록시 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreateEvent(Post event) throws JsonProcessingException {
        kafkaProducer.publishPointUpdateEvent(event); // 포인트 적립
    }


    /*
     * 사용자 행동 패턴 (조회, 댓글, 좋아요)
     */
    public void handleUserBehaviorEvent(String type, PopularPostModel.PostUserBehaviorMessageModel message) {
        rabbitmqProducer.publishPostUserBehaviorEvent(type, message);
    }
}
