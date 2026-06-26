package com.tool_rental.reservas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Reservas")
                        .version("1.0")
                        .description("DocumentaciÃ³n de endpoints para gestiÃ³n de reservas, tipos de reserva, mÃ©todos de pago y reseÃ±as."));
    }
}


