package com.maciezie.ldi.flights;

import com.maciezie.ldi.flights.domain.FlightsDto;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(value = "/flights", produces = APPLICATION_JSON)
public class FlightsController {

    private final FlightsRepository flightsRepository;

    public FlightsController(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    @Get("{?offset,max}")
    public FlightsDto findFlights(
            @QueryValue Optional<Integer> offset,
            @QueryValue Optional<Integer> max) {
        return new FlightsDto(flightsRepository.list().stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(Integer.MAX_VALUE))
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
}
