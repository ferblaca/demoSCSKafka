package com.example.demoStreamKafka;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoStreamKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoStreamKafkaApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(StreamBridge streamBridge) {
        return args -> {
            // Send a message with a specific key
            for (int i = 0; i < 10; i++) {
                streamBridge.send("foo-out-0", new ProductCompleteDTO((long) i, "name", "description", 1.0, 1, "category"));
            }
        };
    }


}
