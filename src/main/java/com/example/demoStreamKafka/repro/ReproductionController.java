package com.example.demoStreamKafka.repro;

import com.example.demoStreamKafka.dto.ProductSimpleDTO;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

	@GetMapping("/stop")
	public ResponseEntity<Map<String, Object>> stopConsumer() {
		this.bindingsLifecycleController.stop(CONSUMER_BINDING);
		return ResponseEntity.ok(this.status("stopped"));
	}

	@GetMapping("/start")
	public ResponseEntity<Map<String, Object>> startConsumer() {
		this.bindingsLifecycleController.start(CONSUMER_BINDING);
		return ResponseEntity.ok(this.status("started"));
	}

	@GetMapping("/send")
	public ResponseEntity<Map<String, Object>> sendMessage() {
		final ProductSimpleDTO product = new ProductSimpleDTO();
		product.setId(System.currentTimeMillis());
		product.setName("reproduction");
		product.setDescription("Message sent after stopping and starting the consumer");
		final boolean sent = this.streamBridge.send("foo-out-0", product);
		return ResponseEntity.ok(Map.of("sent", sent, "receivedMessages", this.receivedMessages.get()));
	}

	@GetMapping("/query-states")
	public ResponseEntity<Map<String, Object>> queryStates() {
		final var bindings = this.bindingsLifecycleController.queryStates();
		return ResponseEntity.ok(Map.of("bindingCount", bindings.size(), "bindings", bindings));
	}

	@GetMapping("/programmatic-restart")
	public ResponseEntity<Map<String, Object>> programmaticRestart() {
		final var bindings = this.bindingsLifecycleController.queryStates();
		final var inputBindingNames = bindings.stream()
				.filter(binding -> Boolean.TRUE.equals(binding.get("input")))
				.map(binding -> binding.get("bindingName").toString())
				.collect(Collectors.toList());

		inputBindingNames.forEach(this.bindingsLifecycleController::stop);
		inputBindingNames.forEach(this.bindingsLifecycleController::start);

		return ResponseEntity.ok(Map.of("bindings", inputBindingNames, "receivedMessages", this.receivedMessages.get()));
	}

	@GetMapping("/status")
	public ResponseEntity<Map<String, Object>> status() {
		return ResponseEntity.ok(this.status("status"));
	}

	@GetMapping("/reset")
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
