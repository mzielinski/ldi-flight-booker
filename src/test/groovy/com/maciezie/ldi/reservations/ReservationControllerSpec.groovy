package com.maciezie.ldi.reservations

import com.maciezie.ldi.jwt.BaseAuthenticationSpec
import com.maciezie.ldi.reservations.domain.ReservationDto
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException

class ReservationControllerSpec extends BaseAuthenticationSpec {

    ReservationDto reservation = new ReservationDto(1, 'Maciej', 'Zieliński', 'XXX')

    def 'should successfully create reservation'() {
        when:
        HttpResponse<ReservationDto> response = jwtClient.createRerservation(token, reservation)

        then:
        response.status() == HttpStatus.CREATED
    }

    def 'should reject request when token is invalid'() {
        when:
        jwtClient.createRerservation('invalid-token', reservation)

        then:
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.message == "Client '/': Unauthorized"
    }
}
