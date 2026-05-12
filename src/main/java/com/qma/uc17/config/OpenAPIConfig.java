package com.qma.uc17.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * UC17 — Swagger / OpenAPI 3 configuration.
 * UI available at: http://localhost:8085/swagger-ui/index.html
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI qmaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("QMA Measurement REST API")
                .description(
                    "UC17 — Spring Boot Backend\n\n" +
                    "CRUD REST API for Quantity Measurement App (QMA). " +
                    "Supports LENGTH, WEIGHT, VOLUME and TEMPERATURE categories.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("BridgeLabz QMA")
                    .email("love@qma.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server().url("http://localhost:8085").description("Local Dev Server")
            ));
    }
}
