package com.maciezie.ldi.flights.persistence;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface FlightsRepository extends CrudRepository<FlightEntity, Integer> {

    // blocking
    List<FlightEntity> findAll();

    // non-blocking
    Flux<FlightDto> list();

    // filtering
    List<FlightDto> findAllByDepartureCityAndArrivalCity(String departure, String arrival);
}
