package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void namesFlux_flatMapAsync() {
        //given
        int stringLength = 4;

        //when
        Flux<String> namesFlux_flatMappedAsync = fluxAndMonoGeneratorService.namesFlux_flatMapAsync(stringLength);

        //then
        StepVerifier.create(namesFlux_flatMappedAsync)
                .expectNextCount(11)
                .verifyComplete();

//        StepVerifier.create(namesFlux_flatMappedAsync)
//                .expectNext("M","I","K","E","Y","M","Y","K","O","L","A") // this will not work with async - at this point
//                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() { //use concatMap if ordering matters
        //given
        int stringLength = 4;

        //when
        Flux<String> namesFlux_concatMap = fluxAndMonoGeneratorService.namesFlux_concatMap(stringLength);

        //then
        StepVerifier.create(namesFlux_concatMap)
                .expectNextCount(11)
                .verifyComplete();

        StepVerifier.create(namesFlux_concatMap)
                .expectNext("M","I","K","E","Y","M","Y","K","O","L","A")
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        //given
        int stringLength = 4;

        //when
        Mono<List<String>> listMono = fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);

        //then
        StepVerifier.create(listMono)
                .expectNext(List.of("M","I","K","E","Y"))
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {
        //given
        int stringLength = 4;

        //when
        Flux<String> listMono = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);

        //then
        StepVerifier.create(listMono)
                .expectNext("M","I","K","E","Y")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        //given
        int stringLength = 4;

        //when
        Flux<String> namesFlux_transformed = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux_transformed)
                .expectNextCount(11)
                .verifyComplete();

        StepVerifier.create(namesFlux_transformed)
                .expectNext("M","I","K","E","Y","M","Y","K","O","L","A")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_1() {
        //given
        int stringLength = 6;

        //when
        Flux<String> namesFlux_transformed = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
//        StepVerifier.create(namesFlux_transformed)
//                .expectNextCount(11)
//                .verifyComplete();

        StepVerifier.create(namesFlux_transformed)
                //.expectNext("M","I","K","E","Y","M","Y","K","O","L","A")
                .expectNext("default")
                .verifyComplete();

    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        //given
        int stringLength = 6;

        //when
        Flux<String> namesFlux_transformedWithSwitchIfEmpty = fluxAndMonoGeneratorService.namesFlux_transform_switchIfEmpty(stringLength);

        //then
//        StepVerifier.create(namesFlux_transformed)
//                .expectNextCount(11)
//                .verifyComplete();

        StepVerifier.create(namesFlux_transformedWithSwitchIfEmpty)
                //.expectNext("M","I","K","E","Y","M","Y","K","O","L","A")
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();

    }

    @Test
    void exploreConcat() {
        //given

        //when
        Flux<String> exploreConcat = fluxAndMonoGeneratorService.exploreConcat();

        //then
        StepVerifier.create(exploreConcat)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWith() {
        //given

        //when
        Flux<String> exploreConcatWith = fluxAndMonoGeneratorService.exploreConcatWith();

        //then
        StepVerifier.create(exploreConcatWith)
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void exploreConcatWith_mono() {
        //given

        //when
        Flux<String> exploreConcatWith_mono = fluxAndMonoGeneratorService.exploreConcatWith_mono();

        //then
        StepVerifier.create(exploreConcatWith_mono)
                .expectSubscription()
                .expectNext("A", "B")
                .verifyComplete();
    }
}