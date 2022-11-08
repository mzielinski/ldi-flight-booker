package com.maciezie.ldi.flights.kafka;

import com.maciezie.ldi.flights.domain.FlightDto;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

import java.util.UUID;

@KafkaClient(id = "flight-notifier")
public interface FlightsNotifier {

    @Topic("flight-uploader")
    void send(@KafkaKey UUID flightId, FlightDto flight);
}
