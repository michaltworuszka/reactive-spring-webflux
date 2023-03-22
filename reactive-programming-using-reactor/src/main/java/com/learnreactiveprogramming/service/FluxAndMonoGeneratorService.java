package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
