package com.maciezie.ldi

import com.maciezie.ldi.flights.domain.FlightDto
import com.maciezie.ldi.flights.domain.FlightsFaker
import com.maciezie.ldi.flights.persistence.FlightsRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class FlightsSpec extends Specification {

    public static final int NUMBER_OF_FLIGHTS = 10

    @Inject
    @Client("/flights")
    HttpClient client

    @Inject
    EmbeddedApplication<?> application

    @Inject
    FlightsFaker flightsFaker

    @Inject
    FlightsRepository repository

    def setup() {
        flightsFaker.initializeWith(NUMBER_OF_FLIGHTS)
    }

    def cleanup() {
        repository.deleteAll()
    }

    void 'should return list of all flights'() {
        when:
        HttpResponse<List<FlightDto>> exchange = client.toBlocking().exchange('', List<FlightDto>)

        then:
        exchange.status() == HttpStatus.OK
        exchange.body().size() == NUMBER_OF_FLIGHTS
    }
}
