package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

        return Flux.fromIterable(List.of("mikey", "mykola", "kris")) //creates a Flux from collection like List
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("sandeep").log();
    }

    public Flux<String> namesFlux_map(int stringLength) {

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                //.map(name -> name.toUpperCase());
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                .map(s -> s.length() + "-" + s)
                .log();
    }

    public Flux<String> namesFlux_immutability() {

        Flux<String> namesFlux = Flux.fromIterable(List.of("mikey", "mykola", "kris"));
        namesFlux.map(String::toUpperCase);
        return namesFlux.log(); //this will return lowerCase - because we map this flux but we didn't introduce the new mapped flux to any variable,
        // so we are returning the unchanged one
    }

    public Flux<String> namesFlux_flatMap(int stringLength) {

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                //MIKEY, MYKOLA -> M,I,K,E,Y,M,Y,K,O,L,A
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitStringWithDelay(String name){
        var charArray = name.split("");
//        int delay = new Random().nextInt(1000);
        int delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
    }

    public Flux<String> namesFlux_flatMapAsync(int stringLength) {

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                //MIKEY, MYKOLA -> M,I,K,E,Y,M,Y,K,O,L,A
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    public Flux<String> namesFlux_concatMap(int stringLength) {

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                //MIKEY, MYKOLA -> M,I,K,E,Y,M,Y,K,O,L,A
                .concatMap(this::splitStringWithDelay) //IF ORDERING MATTERS - USE concatMap
                //use this 'concatMap' for asynchronous operations - when the order is need to be preserved
                //but the overall time it's going to take is going to be higher than flatMap
                .log();
    }


    public Mono<List<String>> namesMono_flatMap(int stringLength) { //flatMap in mono if you get i.e. mono of list
        return Mono.just("mikey")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) { //flatMapMany on Mono is used when method called in it returns Flux, and we need to return a Flux
        return Mono.just("mikey")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] charArray = s.split("");
        List<String> charList = List.of(charArray);
        return Mono.just(charList);
    }


    // **********************  transform + switchIfEmpty  **********************


    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase) // used to get functionality out of reactive stream and reuse it
                .filter(string -> string.length() > stringLength);

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .transform(filterMap)
                .flatMap(this::splitString) //MIKEY, MYKOLA -> M,I,K,E,Y,M,Y,K,O,L,A
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase) // used to get functionality out of reactive stream and reuse it
                .filter(string -> string.length() > stringLength)
                .flatMap(this::splitString);

        Flux<String> defaultFlux = Flux.just("default")
                .transform(filterMap); //will return "D","E","F","A","U","L","T"

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }



    // **********************  concat  **********************
    // with concats subscribers (Flux, Mono) are subscribe in sequence. when one finishes then second "starts".

    public Flux<String> exploreConcat(){

        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return Flux.concat(abcFlux,defFlux).log();
    }

    public Flux<String> exploreConcatWith(){

        Flux<String> abcFlux = Flux.just("A", "B", "C");

        Flux<String> defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> exploreConcatWith_mono(){

        Mono<String> aMono = Mono.just("A");

        Mono<String> bMono = Mono.just("B");

        return aMono.concatWith(bMono).log(); // A, B
    }

    // **********************  merge  **********************
    // in merges subscribers are subscrife eagerly ("at the same time") and merge happens in an interleaved fashion

    public Flux<String> exploreMerge(){

        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux,defFlux).log();

    }

    public Flux<String> exploreMergeWith(){

        Flux<String> abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));

        Flux<String> defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux).log();

    }

    public Flux<String> exploreMergeWithMono(){

        Mono<String> aMono = Mono.just("A");

        Mono<String> dMono = Mono.just("D");

        return aMono.mergeWith(dMono).log();

    }

    // **********************  mergeSequential  **********************



    // **********************  MAIN  **********************

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name ->
                        System.out.println("Name is: " + name));
        fluxAndMonoGeneratorService.nameMono()
                .subscribe(name -> System.out.println("Mono name is: " + name));

        fluxAndMonoGeneratorService.namesFlux_map(2)
                .subscribe(name ->
                        System.out.println("Mapped name is: " + name));
    }
}
