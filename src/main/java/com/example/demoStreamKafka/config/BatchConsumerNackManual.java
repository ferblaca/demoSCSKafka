package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class BatchConsumerNackManual implements Consumer<Message<List<ProductCompleteDTO>>> {

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BatchConsumerNackManual.class);

	@Override
	public void accept(final Message<List<ProductCompleteDTO>> events) {

		final Acknowledgment acknowledgment = events.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT,
				Acknowledgment.class);
		LOG.info("Generic Batch Consumer - Received {} events", events.getPayload().size());
		final int size = events.getPayload().size();
		int i = 0;
		try {
			for (i = 0; i < size; i++) {
				if (i == 20) {
					throw new RuntimeException("Simulated exception at index 20");
				}
			}
		} catch (final RuntimeException e) {
			acknowledgment.nack(i, Duration.ofMillis(100L));
			LOG.error("Error while processing Batch Consumer events", e);
			throw e;
		}
	}

}
