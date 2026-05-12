package com.qma.auth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/** UC21 Auth Microservice — Swagger config with JWT Bearer scheme. */
@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("QMA Auth Service API")
                .description("UC21 Microservice — user registration, login, JWT auth.\n\n" +
                    "1. POST /api/auth/register → get token\n" +
                    "2. POST /api/auth/login → get token\n" +
                    "3. Click Authorize → paste Bearer token")
                .version("1.0.0"))
            .servers(List.of(
                new Server().url("http://localhost:8083").description("Direct"),
                new Server().url("http://localhost:8080").description("Via API Gateway")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
