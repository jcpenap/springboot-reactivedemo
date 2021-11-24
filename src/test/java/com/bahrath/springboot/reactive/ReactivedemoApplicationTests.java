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

import java.time.Duration;
import java.util.Arrays;

@SpringBootTest
class ReactivedemoApplicationTests {

	@Autowired
	VaccineProvider provider;

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
