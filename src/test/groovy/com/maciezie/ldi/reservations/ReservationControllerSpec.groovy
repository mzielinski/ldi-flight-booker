package com.maciezie.ldi.reservations

import com.github.javafaker.Faker
import com.maciezie.ldi.global.RestApiResponse
import com.maciezie.ldi.jwt.BaseAuthenticationSpec
import com.maciezie.ldi.reservations.domain.ReservationDto
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Inject

import static io.micronaut.http.HttpRequest.POST
import static io.micronaut.http.HttpStatus.BAD_REQUEST
import static io.micronaut.http.HttpStatus.CREATED

class ReservationControllerSpec extends BaseAuthenticationSpec {

    ReservationDto reservation = new ReservationDto(
            Faker.instance().number().randomDigit(),
            Faker.instance().name().firstName(),
            Faker.instance().name().lastName(),
            Faker.instance().idNumber().valid())

    @Inject
    ReservationNotificationsObserver notificationsObserver

    @Inject
    @Client("/")
    HttpClient httpClient

    def setup() {
        println "token: $token"
    }

    def 'should successfully create reservation'() {
        when:
        HttpResponse<RestApiResponse> response = jwtClient.createReservation(token, reservation)

        then:
        response.status() == CREATED

        and:
        conditions.eventually {
            assert notificationsObserver.reservations.contains(reservation)
        }
    }

    def 'should reject request when token is invalid'() {
        given:
        HttpRequest<ReservationDto> request = POST('/reservations', reservation)
                .header('Authorization', 'invalid-token')

        when:
        httpClient.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.message == "Client '/': Unauthorized"
    }

    def 'should return proper error message when request is not valid'() {
        given:
        HttpRequest<ReservationDto> request = POST('/reservations', reservation.withFlightId(null))
                .header('Authorization', token)

        when:
        httpClient.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.message == "Client '/': Bad Request"
        e.status == BAD_REQUEST

        and:
        with(e.response.getBody(String)) {
            assert it.isPresent()
            assert it.get() == '{"errorMessage":"Fight identifier cannot be null"}'
        }
    }
}
