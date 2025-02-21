package com.hopoong.email.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;



@Configuration
@EnableKafka
public class KafkaBatchConfig {

    @Bean
    @Qualifier("batchConsumerFactory")
    public ConsumerFactory<String, Object> batchConsumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        // 배치 소비 최적화
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100); // 한 번에 500개 메시지 가져오기
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024); // 1KB로 변경하여 작은 메시지도 가져오도록 설정
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 300); // 300ms 대기 후 가져오기
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000); // 세션 타임아웃 45초

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaBatchListenerContainerFactory(
            @Qualifier("batchConsumerFactory") ConsumerFactory<String, Object> batchConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(batchConsumerFactory);
        factory.setBatchListener(true);
        return factory;
    }
}



