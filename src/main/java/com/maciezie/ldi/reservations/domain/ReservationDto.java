package com.maciezie.ldi.reservations.domain;

import com.maciezie.ldi.global.RestApiResponse;

public record ReservationDto(
        Integer flightId,
        String firstName,
        String lastName,
        String passportNumber) implements RestApiResponse {

    public ReservationDto withFlightId(Integer flightId) {
        return new ReservationDto(flightId, firstName, lastName, passportNumber);
    }
}
