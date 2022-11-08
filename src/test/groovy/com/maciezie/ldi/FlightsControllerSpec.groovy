package com.maciezie.ldi

import com.maciezie.ldi.flights.domain.FlightDto
import com.maciezie.ldi.flights.domain.FlightsDto
import com.maciezie.ldi.flights.persistence.FlightsRepository
import com.maciezie.ldi.flights.utils.FlightsFaker
import com.maciezie.ldi.jwt.BaseAuthenticationSpec
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import spock.lang.Unroll

import static java.util.Optional.empty
import static java.util.Optional.ofNullable

class FlightsControllerSpec extends BaseAuthenticationSpec {

    static int NUMBER_OF_FLIGHTS

    @Inject
    FlightsFaker flightsFaker

    @Inject
    FlightsRepository repository

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
        response.status() == HttpStatus.OK
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
        response.status == HttpStatus.OK
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
        response.status() == HttpStatus.OK

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
}
