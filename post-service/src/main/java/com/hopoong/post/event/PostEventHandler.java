package com.hopoong.post.event;

import com.hopoong.post.adapter.kafka.KafkaProducer;
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
    public void handlePostEvent(String event) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        kafkaProducer.sendMessage(event);
    }

}
