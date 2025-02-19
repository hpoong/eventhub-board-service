package com.hopoong.notification.adapter.kafka;

import com.hopoong.core.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    @KafkaListener(
            topics = KafkaTopic.POST,
            groupId = "post-topic-notification-group",
            concurrency = "1"
    )
    public void listen(ConsumerRecord<String, String> message) {
        log.info("notification-service ::: {}", message.value());
    }

}
