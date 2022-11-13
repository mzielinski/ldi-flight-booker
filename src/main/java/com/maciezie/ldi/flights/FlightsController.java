package com.maciezie.ldi.flights;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightsDto;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import com.maciezie.ldi.flights.persistence.ReactiveFlightsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(value = "/flights", produces = APPLICATION_JSON)
public class FlightsController {

    private final FlightsRepository flightsRepository;
    private final ReactiveFlightsRepository reactiveFlightsRepository;

    public FlightsController(FlightsRepository flightsRepository, ReactiveFlightsRepository reactiveFlightsRepository) {
        this.flightsRepository = flightsRepository;
        this.reactiveFlightsRepository = reactiveFlightsRepository;
    }

    @Get("/reactive{?offset,max,wait}")
    public Flux<FlightDto> findFlightsUsingReactor(
            @QueryValue Optional<Integer> offset,
            @QueryValue Optional<Integer> max,
            @QueryValue Optional<Duration> wait) {
        return reactiveFlightsRepository.list()
                .skip(offset.orElse(0))
                .take(max.orElse(Integer.MAX_VALUE))
                .map(entity -> {
                    blockFor(wait.orElse(Duration.ZERO));
                    return entity;
                });
    }

    @Get("{?offset,max,wait}")
    public FlightsDto findFlights(
            @QueryValue Optional<Integer> offset,
            @QueryValue Optional<Integer> max,
            @QueryValue Optional<Duration> wait) {
        return new FlightsDto(flightsRepository.list().stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(Integer.MAX_VALUE))
                .peek(entity -> blockFor(wait.orElse(Duration.ZERO)))
                .toList());
    }

    // 1. TODO: Add method to find all flights from specific departure (but without arrival city)

    @Get("/departure/{departure}/arrival/{arrival}")
    public FlightsDto findSpecificFlights(
            @PathVariable String departure,
            @PathVariable String arrival) {
        return new FlightsDto(flightsRepository.findAllByDepartureCityAndArrivalCity(departure, arrival));
    }

    // 3. TODO: Create method to create flight via REST API

    private static void blockFor(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
