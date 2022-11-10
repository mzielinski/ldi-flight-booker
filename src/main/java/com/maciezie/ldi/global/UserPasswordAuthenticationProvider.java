package com.maciezie.ldi.global;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Optional;

@Singleton
public class UserPasswordAuthenticationProvider implements AuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(UserPasswordAuthenticationProvider.class);

    public Publisher<AuthenticationResponse> authenticate(
            HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emitter -> {
            Object identity = authenticationRequest.getIdentity();
            LOG.info("User {} tries to login...", identity);
            if (isValidUser(identity, authenticationRequest.getSecret())) {
                emitAccepted(emitter, (String) identity);
                return;
            }
            emitRejected(emitter);
        }, BackpressureStrategy.ERROR);
    }

    private static void emitRejected(FlowableEmitter<AuthenticationResponse> emitter) {
        emitter.onError(new AuthenticationException(new AuthenticationFailed("Wrong username or password")));
    }

    private static void emitAccepted(FlowableEmitter<AuthenticationResponse> emitter, String identity) {
        emitter.onNext(() ->
                Optional.of(new ServerAuthentication(identity, new ArrayList<>(), null)));
        emitter.onComplete();
    }

    private static boolean isValidUser(Object identity, Object secret) {
        return "my-user".equals(identity) && "my-password".equals(secret);
    }
}
