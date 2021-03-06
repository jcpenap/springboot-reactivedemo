package com.bahrath.springboot.reactive;

import com.bahrath.springboot.reactive.vaccine.OrderConsumer;
import com.bahrath.springboot.reactive.vaccine.Product;
import com.bahrath.springboot.reactive.vaccine.Vaccine;
import com.bahrath.springboot.reactive.vaccine.VaccineProvider;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReactivedemoApplicationTests {

	@Autowired
	VaccineProvider provider;

	@Test
	void testVaccineProvider_reactive() {
		StepVerifier.create(provider.provideVaccines())
				.expectSubscription()
				.expectNext(Vaccine.builder().name("Pfizer").build())
				.expectNext(Vaccine.builder().name("J&J").build())
				.expectNext(Vaccine.builder().name("Covaxin").build())
				.expectComplete()
				.verify();
	}

	@Test
	void testVaccineProvider_reactive_expectNextCount() {
		StepVerifier.create(provider.provideVaccines())
				.expectSubscription()
				.expectNextCount(3)
				.expectComplete()
				.verify();
	}

	@Test
	void testVaccineProvider_reactive_assertThat() {
		StepVerifier.create(provider.provideVaccines())
				.expectSubscription()
				.assertNext(vaccine -> {
					assertThat(vaccine.getName()).isNotNull();
					assertTrue(vaccine.isDelivered());
					assertEquals("Pfizer", vaccine.getName());
				})
				.expectNext(Vaccine.builder().name("J&J").build())
				.expectNext(Vaccine.builder().name("Covaxin").build())
				.expectComplete()
				.verify();
	}

	@Test
	void testVaccineProvider_reactive_expectNextMatches() {
		StepVerifier.create(provider.provideVaccines())
				.expectSubscription()
				.expectNextMatches(vaccine -> {
					assertThat(vaccine.getName()).isNotNull();
					assertTrue(vaccine.isDelivered());
					assertEquals("Pfizer", vaccine.getName());
					return true;
				})
				.expectNext(Vaccine.builder().name("J&J").build())
				.expectNext(Vaccine.builder().name("Covaxin").build())
				.expectComplete()
				.verify();
	}

	@Test
	void testVaccineProvider_reactive_thenConsumeWhile() {
		StepVerifier.create(provider.provideVaccines())
				.expectSubscription()
				.expectNextMatches(vaccine -> {
					assertThat(vaccine.getName()).isNotNull();
					assertTrue(vaccine.isDelivered());
					assertEquals("Pfizer", vaccine.getName());
					return true;
				})
				.thenConsumeWhile(vaccine -> {
					System.out.println("Vaccine name while consuming: "+vaccine.getName());
					return true;
				})
				.expectComplete()
				.verify();
	}


	@Test
	void testVaccineProvider() {
		provider.provideVaccines().subscribe(new VaccineConsumer());
	}

	@Test
	void testMono() {
		Mono<Product> mono = Mono.just(new Product("Mackbook Pro"));
		mono.log().map(data -> data.getName().toUpperCase())
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

	@Test
	void testFluxWithSubscriber() throws InterruptedException {
		Flux.fromIterable(Arrays.asList("Mackbook Pro", "Iphone", "Dell","Mackbook Pro", "Iphone", "Dell","Mackbook Pro", "Iphone", "Dell"))
				//.delayElements(Duration.ofSeconds(2))
				.log()
				.map(data -> data.toUpperCase())
				.subscribe(new Subscriber<String>() {

					private long count = 0;
					private Subscription subscription;

					@Override
					public void onSubscribe(Subscription subscription) {
						this.subscription = subscription;
						subscription.request(3);
					}

					@Override
					public void onNext(String order) {
						count++;
						if (count >= 3) {
							count = 0;
							subscription.request(3);
						}
						System.out.println(order);
					}

					@Override
					public void onError(Throwable t) {
						t.printStackTrace();
					}

					@Override
					public void onComplete() {
						System.out.println("Completely Done!!");
					}
				});

		Thread.sleep(7000);
	}

}
