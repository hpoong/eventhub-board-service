package com.hopoong.user.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.kafka")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    @Primary
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler kafkaErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
//        factory.setCommonErrorHandler(kafkaErrorHandler); // 재시도 정책 설정
        factory.setConcurrency(1);
        return factory;
    }


    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {

        // 재시도 정책 설정
        ExponentialBackOffWithMaxRetries backOffPolicy = new ExponentialBackOffWithMaxRetries(5);
        backOffPolicy.setInitialInterval(1000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(15000);


        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate, (record, exception) -> {
                    String dlqTopic = "error-topic.DLQ";
                    return new TopicPartition(dlqTopic, record.partition());
                }),
                backOffPolicy
        );
        defaultErrorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            System.err.println("메시지 재시도 [" + deliveryAttempt + "]회 실패: " + record.value());
        });

        return defaultErrorHandler;
    }

    @Bean
    @Primary
    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, ?> kafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }


}
