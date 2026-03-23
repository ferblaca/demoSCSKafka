package com.example.demoStreamKafka.config;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
public class ConfigurationApp {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigurationApp.class);

	@Bean
	public BatchConsumer batchConsumer() {
		return new BatchConsumer();
	}

	@Bean
	public BatchConsumerBatchListenerException batchConsumerBatchListenerException() {
		return new BatchConsumerBatchListenerException();
	}

	@Bean
	public BatchConsumerAckManual batchConsumerAckManual() {
		return new BatchConsumerAckManual();
	}

	@Bean
	public BatchConsumerNackManual batchConsumerNackManual() {
		return new BatchConsumerNackManual();
	}

	@Bean
	public CustomErrorBatchConsumer customErrorBatchConsumer() {
		return new CustomErrorBatchConsumer();
	}

	@Bean
	public CommonErrorHandler customBatchConsumerCommonErrorHandler() {
		return new DefaultErrorHandler(
				(consumerRecord, e) -> LOG.error("Error processing record: {}", consumerRecord, e));
	}

}
