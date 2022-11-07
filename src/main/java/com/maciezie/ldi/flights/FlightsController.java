package com.maciezie.ldi.flights;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(value = "/flights", produces = APPLICATION_JSON)
public class FlightsController {

    private final FlightsRepository flightsRepository;

    public FlightsController(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    @Get("{?max,offset}")
    public List<FlightDto> getAllFlights(
            @QueryValue Optional<Integer> max,
            @QueryValue Optional<Integer> offset) {
        return flightsRepository.list().stream()
                .skip(offset.orElse(0))
                .limit(max.orElse(Integer.MAX_VALUE))
                .toList();
    }

    @Get("/departure/{departure}/arrival/{arrival}")
    public List<FlightDto> find(
            @PathVariable String departure,
            @PathVariable String arrival) {
        return flightsRepository.findAllByDepartureCityAndArrivalCity(departure, arrival);
    }
}
