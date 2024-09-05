package com.example.demoStreamKafka;

import com.example.demoStreamKafka.dto.ProductCompleteDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@SpringBootApplication
public class DemoStreamKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoStreamKafkaApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            // create instance for properties to access producer configs
            Producer<String, ProductCompleteDTO> kafkaProducer = getKafkaProducer();
            // send a message
            kafkaProducer.send(new ProducerRecord<>("input-topic", "key", new ProductCompleteDTO(1L, "name", "description", 1.0, 1, "category")));
        };
    }

    private Producer<String, ProductCompleteDTO> getKafkaProducer() {
        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", JsonSerializer.class.getName());

        return new KafkaProducer<>(props);
    }

}
