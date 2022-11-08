package com.maciezie.ldi.jwt

import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static io.micronaut.http.HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER

@MicronautTest
abstract class BaseAuthenticationSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    @Inject
    @Client("/")
    ApiClient jwtClient

    String token

    PollingConditions conditions = new PollingConditions()

    def setup() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials('my-user', 'my-password')
        BearerAccessRefreshToken loginResponse = jwtClient.login(credentials)
        token = "$AUTHORIZATION_PREFIX_BEARER ${loginResponse.accessToken}"
    }
}
