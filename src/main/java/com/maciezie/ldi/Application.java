package com.maciezie.ldi;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "flights", version = "0.0"))
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
