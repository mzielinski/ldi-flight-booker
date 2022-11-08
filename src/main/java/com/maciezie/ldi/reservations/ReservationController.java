package com.maciezie.ldi.reservations;

import com.maciezie.ldi.flights.utils.FlightsFaker;
import com.maciezie.ldi.global.CustomerError;
import com.maciezie.ldi.global.RestApiResponse;
import com.maciezie.ldi.reservations.domain.ReservationDto;
import com.maciezie.ldi.reservations.domain.ReservationEntity;
import com.maciezie.ldi.reservations.kafka.ReservationsNotifier;
import com.maciezie.ldi.reservations.persistence.ReservationRepository;
import io.micronaut.http.HttpResponse;
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
    private final ReservationsNotifier notifier;

    public ReservationController(ReservationRepository repository, ReservationsNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    @Status(CREATED)
    @Post
    public HttpResponse<RestApiResponse> createReservation(@Body ReservationDto reservation) {
        if (reservation.flightId() == null) {
            return HttpResponse.badRequest()
                    .body(new CustomerError("Fight identifier cannot be null"));
        }

        ReservationEntity response = repository.save(convertToEntity(reservation));
        notifier.send(response.getId(), reservation);
        LOG.info("Reservation {} successfully created and forwarded to external systems", response);

        return HttpResponse
                .created("/reservations/%s".formatted(response.getId()))
                .body(new ReservationDto(
                        response.getFlightId(),
                        response.getFirstName(),
                        response.getLastName(),
                        response.getPassportNumber()));
    }

    private static ReservationEntity convertToEntity(ReservationDto reservation) {
        ReservationEntity entity = new ReservationEntity();
        entity.setFlightId(reservation.flightId());
        entity.setFirstName(reservation.firstName());
        entity.setLastName(reservation.lastName());
        entity.setPassportNumber(reservation.passportNumber());
        return entity;
    }
}
