package com.maciezie.ldi.global;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record CustomerErrorDto(String errorMessage) implements RestApiResponse {
}