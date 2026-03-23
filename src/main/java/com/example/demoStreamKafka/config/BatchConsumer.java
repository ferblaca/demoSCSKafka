package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

public class BatchConsumer implements Consumer<Message<List<ProductCompleteDTO>>> {

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BatchConsumer.class);

	@Override
	public void accept(final Message<List<ProductCompleteDTO>> events) {
		LOG.info("Generic Batch Consumer - Received {} events", events.getPayload().size());
		final int size = events.getPayload().size();
		for (int i = 0; i < size; i++) {
			if (i == 50) {
				throw new RuntimeException("Simulated exception at index 50");
			}
		}
	}

}
