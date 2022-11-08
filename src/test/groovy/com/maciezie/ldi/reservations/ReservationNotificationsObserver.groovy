package com.maciezie.ldi.reservations

import com.maciezie.ldi.reservations.domain.ReservationDto
import groovy.util.logging.Slf4j
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import jakarta.inject.Singleton

@Slf4j
@Singleton
class ReservationNotificationsObserver {

    private final List<ReservationDto> reservations = []

    @KafkaListener(offsetReset = OffsetReset.EARLIEST)
    @Topic('reservation-notifications')
    void receive(ReservationDto reservation) {
        reservations.add(reservation)
        log.info('Reservation {} was found on Kafka\'s topic', reservation)
    }

    List<ReservationDto> getReservations() {
        return reservations
    }
}
