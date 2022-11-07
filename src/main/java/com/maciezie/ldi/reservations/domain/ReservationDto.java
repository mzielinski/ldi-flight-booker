package com.maciezie.ldi.reservations.domain;

public record ReservationDto(
        Integer flightId,
        String firstName,
        String lastName,
        String passportNumber) {
}
