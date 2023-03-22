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

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase) // used to get functionality out of reactive stream and reuse it
                .filter(string -> string.length() > stringLength);

        return Flux.fromIterable(List.of("mikey", "mykola", "kris"))
                .transform(filterMap)
                //MIKEY, MYKOLA -> M,I,K,E,Y,M,Y,K,O,L,A
                .flatMap(this::splitString)
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) { //flatMap in mono if you get i.e. mono of list
        return Mono.just("mikey")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        String[] charArray = s.split("");
        List<String> charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) { //flatMapMany on Mono is used when method called in it returns Flux, and we need to return a Flux
        return Mono.just("mikey")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString)
                .log();
    }


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
