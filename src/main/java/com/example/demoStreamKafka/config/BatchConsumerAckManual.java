package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.function.Consumer;

public class BatchConsumerAckManual implements Consumer<Message<List<ProductCompleteDTO>>> {

	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(BatchConsumerAckManual.class);

	@Override
	public void accept(final Message<List<ProductCompleteDTO>> events) {

		final Acknowledgment acknowledgment = events.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT,
				Acknowledgment.class);
		LOG.info("Generic Batch Consumer - Received {} events", events.getPayload().size());
		final int size = events.getPayload().size();
		for (int i = 0; i < size; i++) {
			// partially acknowledge every 10 events
			if ((i + 1) % 20 == 0) {
				acknowledgment.acknowledge(i);
			}

			// when i == 20 then force an error
			if (i == 20) {
				throw new RuntimeException("Simulated exception at index 20");
			}
		}
	}

}
