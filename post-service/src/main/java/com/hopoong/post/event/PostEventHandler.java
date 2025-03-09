package com.hopoong.post.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.core.model.notification.PostNotificationMessage;
import com.hopoong.core.model.popularpost.PostUserBehaviorMessage;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.adapter.rabbitmq.RabbitmqProducer;
import com.hopoong.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PostEventHandler {

    private final KafkaProducer kafkaProducer;
    private final RabbitmqProducer rabbitmqProducer;


    /*
     * 글 등록시 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreateEvent(Post event) throws JsonProcessingException {

        // 포인트 적립
        PointUpdateMessage pointUpdateMessage = new PointUpdateMessage(event.getUserId(), event.getId(), 1000L, LocalDateTime.now(), "post_created");
        kafkaProducer.publishPointUpdateEvent(pointUpdateMessage);

        // 알람
        PostNotificationMessage postNotificationMessage = new PostNotificationMessage(event.getUserId(), event.getId());
        kafkaProducer.publishPostCreateEvent(postNotificationMessage);
    }


    /*
     * 사용자 행동 패턴 (조회, 댓글, 좋아요)
     */
    public void handleUserBehaviorEvent(String type, PostUserBehaviorMessage message) {
        rabbitmqProducer.publishPostUserBehaviorEvent(type, message);
    }
}
