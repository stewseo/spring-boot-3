package org.example.spring.reactive.web;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TestCode {

  @Test
  public void example1() {
    Flux<String> sample = Flux.just("learning", "spring", "boot") //
      .filter(s -> s.contains("spring")) //
      .map(s -> {
        System.out.println(s);
        return s.toUpperCase();
      });

    sample.as(StepVerifier::create) //
      .expectNext("SPRING") //
      .expectComplete() //
      .verify();
  }

  @Test
  public void combine() {
    Flux<String> a = Flux.just("alpha", "bravo");
    Flux<String> b = Flux.just("charlie", "delta");

    a.concatWith(b);
    a.mergeWith(b);
  }
}
