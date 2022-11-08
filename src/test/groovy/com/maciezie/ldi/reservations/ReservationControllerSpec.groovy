package com.maciezie.ldi.reservations

import com.maciezie.ldi.global.RestApiResponse
import com.maciezie.ldi.jwt.BaseAuthenticationSpec
import com.maciezie.ldi.reservations.domain.ReservationDto
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Inject

import static io.micronaut.http.HttpStatus.BAD_REQUEST
import static io.micronaut.http.HttpStatus.CREATED

class ReservationControllerSpec extends BaseAuthenticationSpec {

    ReservationDto reservation = new ReservationDto(1, 'Maciej', 'Zielinski', 'XXX')

    @Inject
    ReservationNotificationsObserver notificationsObserver

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
        when:
        jwtClient.createReservation('invalid-token', reservation)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.message == "Client '/': Unauthorized"
    }

    def 'should return proper error message when request is not valid'() {
        when:
        jwtClient.createReservation(token, reservation.withFlightId(null))

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
