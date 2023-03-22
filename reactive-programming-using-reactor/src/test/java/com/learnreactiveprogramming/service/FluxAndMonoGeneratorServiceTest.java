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
                .expectNext("mikey", "mykola", "kris")
                .expectNextCount(0)//how many you expect after previous method is called
                .verifyComplete();

        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        //given
        int stringLength = 4;

        //when
        Flux<String> namesFlux_mapped = fluxAndMonoGeneratorService.namesFlux_map(stringLength);

        //then
        StepVerifier.create(namesFlux_mapped)
                .expectNext("mikey".length() + "-" + "mikey".toUpperCase() ,"mykola".length() + "-" + "mykola".toUpperCase())
                .verifyComplete();
    }

    @Test
    void namesFlux_immutability() {
        //given

        //when
        Flux<String> namesFlux_immutability = fluxAndMonoGeneratorService.namesFlux_immutability();

        //then
        StepVerifier.create(namesFlux_immutability)
                .expectNext("mikey", "mykola", "kris")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {
        //given
        int stringLength = 4;

        //when
        Flux<String> namesFlux_flatMapped = fluxAndMonoGeneratorService.namesFlux_flatMap(stringLength);

        //then
        StepVerifier.create(namesFlux_flatMapped)
                .expectNextCount(11)
                .verifyComplete();

        StepVerifier.create(namesFlux_flatMapped)
                .expectNext("M","I","K","E","Y","M","Y","K","O","L","A")
                .verifyComplete();
    }
}