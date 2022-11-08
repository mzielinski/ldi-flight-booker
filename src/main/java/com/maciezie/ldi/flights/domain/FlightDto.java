package com.maciezie.ldi.flights.domain;

import com.maciezie.ldi.global.RestApiResponse;
import io.micronaut.core.annotation.Introspected;

import java.time.Instant;

@Introspected
public record FlightDto(
        Integer id,
        String departureCity,
        Instant departureDatetime,
        String arrivalCity,
        Instant arrivalDatetime) implements RestApiResponse {
}
