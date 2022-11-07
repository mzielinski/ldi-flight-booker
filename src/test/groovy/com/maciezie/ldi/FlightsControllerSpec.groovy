package com.maciezie.ldi

import com.fasterxml.jackson.databind.ObjectMapper
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
import spock.lang.Unroll

import java.text.SimpleDateFormat

@MicronautTest
class FlightsControllerSpec extends Specification {

    static int NUMBER_OF_FLIGHTS

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
        NUMBER_OF_FLIGHTS = flightsFaker.initializeWith(10)
    }

    def cleanup() {
        repository.deleteAll()
    }

    def 'should return list of all flights'() {
        when:
        HttpResponse<List> response = client.toBlocking().exchange('', List)

        then:
        response.status() == HttpStatus.OK
        response.body().size() == NUMBER_OF_FLIGHTS
    }

    @Unroll
    def 'should return expected flights when offset is #offset and max is #max'() {
        given:
        List<FlightDto> expectedList = repository.findAll()
                .collect { new FlightDto(it.id, it.departureCity, it.departureDatetime, it.arrivalCity, it.arrivalDatetime) }
                .drop(offset ?: 0)
                .take(max ?: NUMBER_OF_FLIGHTS)

        when:
        HttpResponse<String> response = client.toBlocking().exchange(path as String, String)

        then:
        response.status == HttpStatus.OK
        response.body.get() == createObjectMapper().writeValueAsString(expectedList)

        where:
        offset | max  | path
        5      | null | "?offset=$offset"
        null   | 5    | "?max=$max"
        5      | 1    | "?offset=$offset&max=$max"
        null   | null | ''
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.findAndRegisterModules()
        mapper.dateFormat = new SimpleDateFormat('yyyy-MM-dd HH:mm:ss')
        mapper
    }
}
