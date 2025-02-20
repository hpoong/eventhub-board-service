package com.hopoong.post.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.api.post.model.PostModel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PostEventHandler {

    private final KafkaProducer kafkaProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostEvent(PostModel.CreateRequest event) throws JsonProcessingException {
        kafkaProducer.publishPostEvent(event);
    }
}
