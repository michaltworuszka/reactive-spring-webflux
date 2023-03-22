package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService =
            new FluxAndMonoGeneratorService();


    @Test
    void namesFlux() {
        //given

        //when
        Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
                .expectNext("mike", "mykola", "kris")
                .expectNextCount(0)//how many you expect after previous method is called
                .verifyComplete();

        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }
}