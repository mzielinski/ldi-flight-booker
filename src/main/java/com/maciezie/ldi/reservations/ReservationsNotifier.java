package com.maciezie.ldi.reservations;

import com.maciezie.ldi.reservations.domain.ReservationDto;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient(id = "flight-booker")
public interface ReservationsNotifier {

    @Topic("reservation-notifications")
    void send(@KafkaKey Long reservationId, ReservationDto reservationDto);
}
