package com.qma.measurement.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * UC21 Measurement Microservice — Swagger / OpenAPI 3 config.
 *
 * Direct Swagger UI : http://localhost:8084/swagger-ui.html
 * Via Gateway UI    : http://localhost:8080/swagger-ui.html -> select "measurement-service"
 *
 * Steps to test:
 *   1. POST /api/auth/login  -> copy the token
 *   2. Click Authorize (lock) -> enter: Bearer <token>
 *   3. All measurement endpoints are now unlocked
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("QMA Measurement Service API")
                .description(
                    "**UC21 Microservice** — CRUD for quantity measurements.\n\n" +
                    "Supported categories: `LENGTH`, `WEIGHT`, `VOLUME`, `TEMPERATURE`\n\n" +
                    "**JWT required** on all endpoints (except `/health`).\n\n" +
                    "**How to authenticate:**\n" +
                    "1. Get token: `POST http://localhost:8083/api/auth/login`\n" +
                    "2. Click **Authorize** -> enter: `Bearer <token>`\n\n" +
                    "Via API Gateway: `http://localhost:8080/api/measurements/**`"
                )
                .version("1.0.0"))
            .servers(List.of(
                new Server().url("http://localhost:8084").description("Direct (port 8084)"),
                new Server().url("http://localhost:8080").description("Via API Gateway (port 8080)")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Paste JWT token from POST /api/auth/login")));
    }
}