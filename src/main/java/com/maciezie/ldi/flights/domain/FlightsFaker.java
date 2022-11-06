package com.maciezie.ldi.flights.domain;

import com.github.javafaker.Faker;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@Singleton
public class FlightsFaker {

    private static final Logger LOG = LoggerFactory.getLogger(FlightsFaker.class);
    private final static Faker FAKER = new Faker();

    private final FlightsRepository repository;

    public FlightsFaker(FlightsRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        initializeWith(10);
    }

    public void initializeWith(int size) {
        repository.deleteAll();
        IntStream.range(0, size)
                .forEach(index -> addNewFlight());
    }

    private void addNewFlight() {
        FlightEntity flight = new FlightEntity();

        // departure
        String departureCity = FAKER.country().capital();
        flight.setDepartureCity(departureCity);
        Instant departureDatetime = Instant.now()
                .plus(FAKER.number().randomDigit(), ChronoUnit.HOURS);
        flight.setDepartureDatetime(departureDatetime);

        // arrival
        flight.setArrivalCity(findArrivalCity(departureCity));
        flight.setArrivalDatetime(departureDatetime
                .plus(FAKER.number().randomDigit(), ChronoUnit.HOURS));

        repository.save(flight);
        LOG.info(String.format("Adding %s to the storage", flight));
    }

    private static String findArrivalCity(String departureCity) {
        String arrivalCity;
        do {
            arrivalCity = FAKER.country().capital();
        } while (arrivalCity.equals(departureCity));
        return arrivalCity;
    }
}
