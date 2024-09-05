package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import com.example.demoStreamKafka.dto.ProductSimpleDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.function.json.JacksonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.function.Consumer;

@Configuration
public class ConfigurationApp {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigurationApp.class);

    @Bean
    public Consumer<ProductSimpleDTO> simpleConsumer() {
        return s -> {
            LOG.info("Received simple Id: {}", s.getId());
        };
    }

    @Bean
    public Consumer<ProductCompleteDTO> completeConsumer() {
        return s -> {
            LOG.info("Received complete Id: {}", s.getId());
        };
    }

    @Configuration
    @ConditionalOnProperty(value = "demo.jackson.mapper.enabled", havingValue = "true", matchIfMissing = true)
    public static class JacksonConfiguration {

        @Bean
        @Primary
        public JacksonMapper jacksonMapper(final ObjectMapper objectMapper) {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
            return new JacksonMapper(objectMapper);
        }

    }

}
