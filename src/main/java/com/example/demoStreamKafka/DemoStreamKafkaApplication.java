package com.example.demoStreamKafka;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
public class DemoStreamKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoStreamKafkaApplication.class, args);
	}

	@Bean
	public ApplicationRunner runner(StreamBridge streamBridge) {
		return args -> {
			// send a message
			for (int i = 0; i < 100; i++) {
				streamBridge.send("foo-out-0", MessageBuilder
						.withPayload(new ProductCompleteDTO(1L, "name", "description", 1.0, 1, "category")).build());
			}
		};
	}

}
