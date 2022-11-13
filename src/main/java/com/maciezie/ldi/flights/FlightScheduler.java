package com.maciezie.ldi.flights;

import com.maciezie.ldi.flights.domain.FlightDto;
import com.maciezie.ldi.flights.kafka.FlightsNotifier;
import com.maciezie.ldi.flights.utils.FlightsFaker;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class FlightScheduler {

    private final FlightsNotifier notifier;

    public FlightScheduler(FlightsNotifier notifier) {
        this.notifier = notifier;
    }

    @Scheduled(fixedDelay = "10s")
    void generate() {
        FlightDto convert = FlightsFaker.createEntity().convert();
        notifier.send(UUID.randomUUID(), convert);
    }
}
