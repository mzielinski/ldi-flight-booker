package com.maciezie.ldi.flights.persistence;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;

@Repository
public interface ReactiveFlightsRepository extends ReactiveStreamsCrudRepository<FlightEntity, Integer> {

    Flux<FlightDto> list();

}
