package com.maciezie.ldi.flights.kafka;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightEntity;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@KafkaListener(clientId = "flight-booker", groupId = "flight-uploader", batch = true)
public class FlightsUploader {

    private final static Logger LOG = LoggerFactory.getLogger(FlightsUploader.class);

    private final FlightsRepository repository;

    public FlightsUploader(FlightsRepository repository) {
        this.repository = repository;
    }

    @Topic("flight-uploader")
    void receive(List<FlightDto> flights) {
        flights.forEach(flight -> {
            FlightEntity entity = repository.save(createEntity(flight));
            LOG.info("New flight {} was stored", entity);
        });
    }

    private static FlightEntity createEntity(FlightDto flight) {
        FlightEntity entity = new FlightEntity();
        entity.setDepartureDatetime(flight.departureDatetime());
        entity.setDepartureCity(flight.departureCity());
        entity.setArrivalDatetime(flight.arrivalDatetime());
        entity.setArrivalCity(flight.arrivalCity());
        return entity;
    }
}
