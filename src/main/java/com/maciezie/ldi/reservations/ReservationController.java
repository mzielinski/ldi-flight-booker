package com.maciezie.ldi.reservations;

import com.maciezie.ldi.flights.domain.FlightsFaker;
import com.maciezie.ldi.reservations.domain.ReservationDto;
import com.maciezie.ldi.reservations.domain.ReservationEntity;
import com.maciezie.ldi.reservations.persistence.ReservationRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.http.HttpStatus.CREATED;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "reservations", produces = MediaType.APPLICATION_JSON)
public class ReservationController {

    private static final Logger LOG = LoggerFactory.getLogger(FlightsFaker.class);

    private final ReservationRepository repository;

    public ReservationController(ReservationRepository repository) {
        this.repository = repository;
    }

    @Status(CREATED)
    @Post
    public void createReservation(@Body ReservationDto reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setFlightId(reservation.flightId());
        entity.setFirstName(reservation.firstName());
        entity.setLastName(reservation.lastName());
        entity.setPassportNumber(reservation.passportNumber());
        ReservationEntity response = repository.save(entity);
        LOG.info("Reservation {} successfully created", response);
    }
}
