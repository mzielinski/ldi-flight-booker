package com.maciezie.ldi.reservations.persistence;

import com.maciezie.ldi.reservations.domain.ReservationEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface ReservationRepository extends CrudRepository<ReservationEntity, Integer> {
}
