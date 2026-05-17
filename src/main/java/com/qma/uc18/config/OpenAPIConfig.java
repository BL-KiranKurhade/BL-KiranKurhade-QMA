package com.qma.uc18.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * UC18 — Swagger / OpenAPI 3 configuration with JWT Bearer auth.
 * UI: http://localhost:8085/swagger-ui/index.html
 *
 * Usage: Click "Authorize" → paste: Bearer <your-jwt-token>
 */
@Configuration
public class OpenAPIConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI qmaSecurityOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("QMA Security API — JWT + Google OAuth2")
                .description(
                    "UC18 — Spring Security + JWT + Google OAuth2\n\n" +
                    "**JWT Auth Flow:**\n" +
                    "1. `POST /api/auth/register` — create account\n" +
                    "2. `POST /api/auth/login` — get JWT token\n" +
                    "3. Click **Authorize** above → enter `Bearer <token>`\n" +
                    "4. All protected endpoints will now include the token automatically\n\n" +
                    "**Google OAuth2 Flow:**\n" +
                    "Open in a **new tab**: `http://localhost:8085/oauth2/authorization/google`\n" +
                    "After login, the token is returned as a URL parameter to the React callback.\n\n" +
                    "> ⚠️ Do NOT click other links while Google OAuth2 sign-in is in progress.")
                .version("1.0.0")
                .contact(new Contact().name("BridgeLabz QMA").url("http://localhost:8085/swagger-ui/index.html")))
            .servers(List.of(
                new Server().url("http://localhost:8085").description("Local Dev — UC18")))
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Paste the JWT token obtained from /api/auth/login")));
    }
}
