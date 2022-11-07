package com.maciezie.ldi.flights.persistence;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.domain.FlightEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface FlightsRepository extends CrudRepository<FlightEntity, Integer> {

    List<FlightDto> list();

    List<FlightDto> findAllByDepartureCityAndArrivalCity(String departure, String arrival);
}
