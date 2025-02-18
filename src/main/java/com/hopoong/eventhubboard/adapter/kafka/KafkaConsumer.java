package com.hopoong.eventhubboard.adapter.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(
            topics = KafkaTopic.TEST,
            groupId = "test-topic-consumer-group",
            concurrency = "1"
    )
    public void listen(ConsumerRecord<String, String> message) {
        System.out.println(message.value());
    }

}
