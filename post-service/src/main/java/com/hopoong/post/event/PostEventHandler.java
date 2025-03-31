package com.hopoong.post.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.core.model.notification.PostNotificationMessage;
import com.hopoong.core.model.popularpost.PostUserBehaviorMessage;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.adapter.rabbitmq.RabbitmqProducer;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.api.post.model.PostEventModel;
import com.hopoong.post.domain.PostEntity;
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
    private final PopularPostRedisService popularPostRedisService;

    /*
     * 글 등록시 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostCreateEvent(PostEntity event) throws JsonProcessingException {

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


    /*
     * 조회, 댓글, 좋아요 이벤트
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserInteractionEvent(PostEventModel.UserInteractionEvent event) {

        // rabbitMQ ::: 사용자 행동 패턴
        this.handleUserBehaviorEvent(event.type(), new PostUserBehaviorMessage(event.postId(), event.userId()));

        // redis ::: 인기 게시글
        popularPostRedisService.incrementRealTimePopularPostCount(event.postId());
    }

}
