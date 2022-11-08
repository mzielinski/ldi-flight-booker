package com.maciezie.ldi.flights

import com.maciezie.ldi.flights.domain.FlightDto
import com.maciezie.ldi.flights.domain.FlightsDto
import com.maciezie.ldi.flights.kafka.FlightsNotifier
import com.maciezie.ldi.flights.persistence.FlightsRepository
import com.maciezie.ldi.flights.utils.FlightsFaker
import com.maciezie.ldi.jwt.BaseAuthenticationSpec
import io.micronaut.http.HttpResponse
import jakarta.inject.Inject
import spock.lang.Unroll

import static com.maciezie.ldi.flights.utils.FlightsFaker.createCapitolName
import static io.micronaut.http.HttpStatus.OK
import static java.time.Instant.now
import static java.time.temporal.ChronoField.NANO_OF_SECOND
import static java.util.Optional.empty
import static java.util.Optional.ofNullable

class FlightsControllerSpec extends BaseAuthenticationSpec {

    static int NUMBER_OF_FLIGHTS

    @Inject
    FlightsFaker flightsFaker

    @Inject
    FlightsRepository repository

    @Inject
    FlightsNotifier notifier

    def setup() {
        NUMBER_OF_FLIGHTS = flightsFaker.initializeWith(10)
    }

    def cleanup() {
        repository.deleteAll()
    }

    def 'should return list of all flights'() {
        when:
        HttpResponse<FlightsDto> response = jwtClient.flights(empty(), empty())

        then:
        response.status() == OK
        response.body().flights().size() == NUMBER_OF_FLIGHTS
    }

    @Unroll
    def 'should return expected flights when offset is #offset and max is #max'() {
        given:
        List<FlightDto> expectedList = repository.findAll()
                .collect { new FlightDto(it.id, it.departureCity, it.departureDatetime, it.arrivalCity, it.arrivalDatetime) }
                .drop(offset ?: 0)
                .take(max ?: NUMBER_OF_FLIGHTS)

        when:
        HttpResponse<FlightsDto> response = jwtClient.flights(ofNullable(offset), ofNullable(max))

        then:
        response.status == OK
        response.body.get().flights() == expectedList

        where:
        offset | max
        5      | null
        null   | 5
        5      | 1
        null   | null
    }

    @Unroll
    def 'should return flight(s) for departure #depature and arrival #arrival'() {
        given:
        List<Integer> expectedIds = repository.findAll()
                .findAll { it.arrivalCity == arrival && it.departureCity == depature }
                .collect { it.id }

        when:
        HttpResponse<FlightsDto> response = jwtClient.flightsForSpecificDirection(depature, arrival)

        then:
        response.status() == OK

        and:
        with(response.body().flights()) {
            it.size() == expectedIds.size()
            it.forEach {
                assert expectedIds.contains(it['id'])
                assert it['departureCity'] == depature
                assert it['arrivalCity'] == arrival
            }
        }

        where:
        depature = 'Warsaw'
        arrival = 'Miami'
    }

    def 'should store new flight data when message is sent to kafka topic'() {
        when:
        notifier.send(
                UUID.randomUUID(),
                new FlightDto(null, depature, depatureDatetime, arrival, arrivalDatetime))

        then:
        conditions.eventually {
            HttpResponse<FlightsDto> flights = jwtClient.flightsForSpecificDirection(depature, arrival)
            with(flights.body().flights()) {
                it.size() == 1
                assert it[0].id() != null
                assert it[0].departureCity() == depature
                assert it[0].departureDatetime() == depatureDatetime
                assert it[0].arrivalCity() == arrival
                assert it[0].arrivalDatetime() == arrivalDatetime
            }
        }

        where:
        depature = createCapitolName()
        depatureDatetime = now().with(NANO_OF_SECOND, 0L)
        arrival = createCapitolName()
        arrivalDatetime = now().with(NANO_OF_SECOND, 0L)
    }
}
