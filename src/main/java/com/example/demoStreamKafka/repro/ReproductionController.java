package com.example.demoStreamKafka.repro;

import com.example.demoStreamKafka.dto.ProductSimpleDTO;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/repro")
public class ReproductionController {

	public static final String CONSUMER_BINDING = "simpleConsumer-in-0";
	private final BindingsLifecycleController bindingsLifecycleController;
	private final StreamBridge streamBridge;
	private final AtomicInteger receivedMessages;

	public ReproductionController(BindingsLifecycleController bindingsLifecycleController, StreamBridge streamBridge) {
		this.bindingsLifecycleController = bindingsLifecycleController;
		this.streamBridge = streamBridge;
		this.receivedMessages = new AtomicInteger();
	}

	@PostMapping("/stop")
	public ResponseEntity<Map<String, Object>> stopConsumer() {
		this.bindingsLifecycleController.stop(CONSUMER_BINDING);
		return ResponseEntity.ok(this.status("stopped"));
	}

	@PostMapping("/start")
	public ResponseEntity<Map<String, Object>> startConsumer() {
		this.bindingsLifecycleController.start(CONSUMER_BINDING);
		return ResponseEntity.ok(this.status("started"));
	}

	@PostMapping("/send")
	public ResponseEntity<Map<String, Object>> sendMessage() {
		final ProductSimpleDTO product = new ProductSimpleDTO();
		product.setId(System.currentTimeMillis());
		product.setName("reproduction");
		product.setDescription("Message sent after stopping and starting the consumer");
		final boolean sent = this.streamBridge.send("foo-out-0", product);
		return ResponseEntity.ok(Map.of("sent", sent, "receivedMessages", this.receivedMessages.get()));
	}

	@PostMapping("/reset")
	public ResponseEntity<Map<String, Object>> reset() {
		this.receivedMessages.set(0);
		return ResponseEntity.ok(this.status("reset"));
	}

	public void messageReceived() {
		this.receivedMessages.incrementAndGet();
	}

	private Map<String, Object> status(String action) {
		return Map.of("action", action, "receivedMessages", this.receivedMessages.get());
	}

}