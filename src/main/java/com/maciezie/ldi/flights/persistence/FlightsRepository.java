package com.maciezie.ldi.flights.persistence;

import com.maciezie.ldi.flights.domain.FlightEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface FlightsRepository extends CrudRepository<FlightEntity, Integer> {

    List<FlightEntity> findAll();
}
