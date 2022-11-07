package com.maciezie.ldi.flights;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightsFaker;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(value = "/flights", produces = APPLICATION_JSON)
public class FlightsController {

    private final FlightsRepository flightsRepository;

    public FlightsController(FlightsRepository flightsRepository) {
        this.flightsRepository = flightsRepository;
    }

    @Get
    public List<FlightDto> get() {
        return flightsRepository.list();
    }
}
