package com.maciezie.ldi.global;

public record CustomerError(String errorMessage) implements RestApiResponse {
}