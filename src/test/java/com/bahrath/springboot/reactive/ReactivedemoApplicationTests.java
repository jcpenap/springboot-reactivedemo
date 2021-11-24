package com.bahrath.springboot.reactive;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@SpringBootTest
class ReactivedemoApplicationTests {

	@Test
	void testMono() {
		Mono<String> mono = Mono.just("Mackbook Pro");
		mono.log().map(data -> data.toUpperCase())
				.subscribe(System.out::println);;
	}

	@Test
	void testFlux() {
		Flux.just("Mackbook Pro", "Iphone", "Dell")
				.log()
				.map(data -> data.toUpperCase())
				.subscribe(new OrderConsumer());
	}

	@Test
	void testFluxFromIterable() throws InterruptedException {
		Flux.fromIterable(Arrays.asList("Mackbook Pro", "Iphone", "Dell"))
				.delayElements(Duration.ofSeconds(2))
				.log()
				.map(data -> data.toUpperCase())
				.subscribe(new OrderConsumer());

		Thread.sleep(7000);
	}

}
