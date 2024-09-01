package com.example.FlightBookingSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    // Basic Authentication scheme name
    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(BASIC_AUTH, new SecurityScheme()
                                .name(BASIC_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                )
                .info(new Info()
                        .title("Flight Booking System API")
                        .description("API documentation for Flight Booking System")
                        .version("1.0")
                );
    }
}
