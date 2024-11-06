package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.function.Consumer;

@Configuration
public class ConfigurationApp {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigurationApp.class);

    @Bean
    public Consumer<Message<ProductCompleteDTO>> completeConsumer1() {
        return s -> {
            MessageHeaders headers = s.getHeaders();
            LOG.info("Received complete1 Id: {}", s.getPayload().getId());
        };
    }

    @Bean
    public Consumer<Message<ProductCompleteDTO>> completeConsumer2() {
        return s -> {
            MessageHeaders headers = s.getHeaders();
            LOG.info("Received complete2 Id: {}", s.getPayload().getId());
        };
    }

}
