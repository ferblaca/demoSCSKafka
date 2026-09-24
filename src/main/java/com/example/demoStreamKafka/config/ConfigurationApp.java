package com.example.demoStreamKafka.config;

import com.example.demoStreamKafka.dto.ProductSimpleDTO;
import com.example.demoStreamKafka.repro.ReproductionController;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class ConfigurationApp {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ConfigurationApp.class);

	@Bean
	public Consumer<ProductSimpleDTO> simpleConsumer(ReproductionController reproductionController) {
		return s -> {
			reproductionController.messageReceived();
			LOG.info("Received simple Id: {}", s.getId());
		};
	}

}
