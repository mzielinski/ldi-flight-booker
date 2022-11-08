package com.maciezie.ldi.flights.domain;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public record FlightsDto(List<FlightDto> flights) {
}
