/*
 * Copyright (c) 2021. Inditex
 */

package com.example.demoStreamKafka.sources;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.binder.kafka.support.ConsumerConfigCustomizer;
import org.springframework.cloud.stream.binder.kafka.support.ProducerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The purpose of this class is to customize the client.id property of kafka producers and consumers.
 *
 * @since 4.0.0
 */
@ConditionalOnProperty(value = "custom-client-id.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaProducerConsumerConfiguration {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(KafkaProducerConsumerConfiguration.class);

    private static final String CUSTOM = "CUSTOM_";

    @Bean
    public ConsumerConfigCustomizer consumerConfigCustomizer() {
        return (consumerProperties, bindingName, destination) -> {
            LOG.info("Customizing consumer client id for binding: {}", bindingName);
            consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, CUSTOM + bindingName);
        };
    }

    @Bean
    public ProducerConfigCustomizer producerConfigCustomizer() {
        return (producerProperties, bindingName, destination) -> {
            LOG.info("Customizing producer client id for binding: {}", bindingName);
            producerProperties.put(ProducerConfig.CLIENT_ID_CONFIG, CUSTOM + bindingName);
        };
    }

}
