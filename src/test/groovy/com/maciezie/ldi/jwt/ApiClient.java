package com.maciezie.ldi.jwt;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightsDto;
import com.maciezie.ldi.reservations.domain.ReservationDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.CircuitBreaker;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Client("/")
@CircuitBreaker
public interface ApiClient {

    @Post("/login")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

    @Get("/flights{?offset,max}")
    HttpResponse<FlightsDto> flights(
            @QueryValue Optional<Integer> offset,
            @QueryValue Optional<Integer> max
    );


    @Get("/flights/reactive{?offset,max}")
    Flux<FlightDto> flightsWithNonBlockingApi(
            @QueryValue Optional<Integer> offset,
            @QueryValue Optional<Integer> max
    );

    @Get("/flights/departure/{departure}/arrival/{arrival}")
    HttpResponse<FlightsDto> flightsForSpecificDirection(
            @PathVariable String departure,
            @PathVariable String arrival);

    @Post("/reservations")
    HttpResponse<ReservationDto> createReservation(
            @Header(name = "Authorization") String authorization,
            @Body ReservationDto reservationDto);
}
