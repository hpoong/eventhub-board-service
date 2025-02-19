package com.hopoong.post.adapter.kafka;

import com.hopoong.core.topic.KafkaTopic;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        kafkaTemplate.send(KafkaTopic.POST, String.valueOf(UUID.randomUUID().toString()), message);
    }
}
