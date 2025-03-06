package com.hopoong.user.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.core.model.post.PointNotificationMessage;
import com.hopoong.user.adapter.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventHandler {

    private final KafkaProducer kafkaProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePointUpdateNotificationEvent(PointNotificationMessage message) throws JsonProcessingException {
        kafkaProducer.publishPointUpdateNotificationEvent(message);
    }


}
