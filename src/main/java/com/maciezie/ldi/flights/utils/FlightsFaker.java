package com.maciezie.ldi.flights.utils;

import com.github.javafaker.Faker;
import com.maciezie.ldi.flights.domain.FlightEntity;
import com.maciezie.ldi.flights.persistence.FlightsRepository;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@Singleton
public class FlightsFaker {

    private static final Logger LOG = LoggerFactory.getLogger(FlightsFaker.class);
    private final static Faker FAKER = new Faker();

    private final FlightsRepository repository;

    public FlightsFaker(FlightsRepository repository) {
        this.repository = repository;
    }

    public int initializeWith(int size) {
        repository.deleteAll();
        IntStream.range(0, size)
                .forEach(index -> addNewFlight(
                        FlightsFaker::createCapitolName,
                        FlightsFaker::createCapitolName));

        addNewFlight(() -> "Warsaw", () -> "Paris");
        addNewFlight(() -> "Warsaw", () -> "Vienna");
        addNewFlight(() -> "Warsaw", () -> "Miami");
        addNewFlight(() -> "Warsaw", () -> "Miami");

        return (int) repository.count();
    }

    public static String createCapitolName() {
        try {
            return FAKER.country().capital();
        } catch (Exception e) {
            // XXX: does not work in native-image, returns null result ???
            return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        }
    }

    public static FlightEntity createEntity() {
        return createEntity(FlightsFaker::createCapitolName, FlightsFaker::createCapitolName);
    }

    private void addNewFlight(Supplier<String> departureCityProducer, Supplier<String> arrivalCityProducer) {
        FlightEntity flight = createEntity(departureCityProducer, arrivalCityProducer);
        repository.save(flight);
        LOG.info(String.format("Adding %s to the storage", flight));
    }

    private static FlightEntity createEntity(Supplier<String> departureCityProducer, Supplier<String> arrivalCityProducer) {
        FlightEntity flight = new FlightEntity();

        // departure
        String departureCity = departureCityProducer.get();
        flight.setDepartureCity(departureCity);
        Instant departureDatetime = Instant.now()
                .plus(FAKER.number().randomDigit(), ChronoUnit.HOURS);
        flight.setDepartureDatetime(departureDatetime);

        // arrival
        flight.setArrivalCity(findArrivalCity(departureCity, arrivalCityProducer));
        flight.setArrivalDatetime(departureDatetime
                .plus(FAKER.number().randomDigit(), ChronoUnit.HOURS));
        return flight;
    }

    private static String findArrivalCity(String departureCity, Supplier<String> cityProducer) {
        String arrivalCity;
        do {
            arrivalCity = cityProducer.get();
        } while (arrivalCity.equals(departureCity));
        return arrivalCity;
    }
}
