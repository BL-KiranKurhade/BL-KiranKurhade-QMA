package com.qma.gateway.config;

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
 * UC21 API Gateway — Swagger / OpenAPI 3 aggregation config.
 *
 * The Gateway aggregates Swagger UI for ALL microservices.
 * Access: http://localhost:8080/swagger-ui.html
 * Use the top-right dropdown to switch between services:
 *   • auth-service       → /v3/api-docs/auth-service
 *   • measurement-service→ /v3/api-docs/measurement-service
 *   • conversion-service → /v3/api-docs/conversion-service
 *
 * How to test any protected endpoint:
 *   1. POST /api/auth/login (no auth needed) → copy the JWT token
 *   2. Click Authorize (🔒) → enter: Bearer <token>
 *   3. Switch to the service you want → all endpoints unlocked
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("QMA API Gateway — Aggregated Swagger UI")
                .description(
                    "**UC21 Microservices** — All APIs accessible through the API Gateway.\n\n" +
                    "**Services available (use dropdown top-right):**\n" +
                    "- `auth-service` — Register / Login / JWT token\n" +
                    "- `measurement-service` — CRUD for quantity measurements\n" +
                    "- `conversion-service` — Unit conversions (LENGTH, WEIGHT, VOLUME, TEMPERATURE)\n\n" +
                    "**How to authenticate:**\n" +
                    "1. Select **auth-service** in the dropdown\n" +
                    "2. Call `POST /api/auth/login` → copy the JWT token\n" +
                    "3. Click **Authorize** 🔒 → paste: `Bearer <token>`\n" +
                    "4. Switch to any other service — all endpoints are now unlocked\n\n" +
                    "**Direct service URLs:**\n" +
                    "- Auth Service: `http://localhost:8083`\n" +
                    "- Measurement Service: `http://localhost:8084`\n" +
                    "- Conversion Service: `http://localhost:8082`\n" +
                    "- Eureka Dashboard: `http://localhost:8761`"
                )
                .version("1.0.0"))
            .servers(List.of(
                new Server().url("http://localhost:8080").description("API Gateway (port 8080)")
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
