package com.qma.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * UC21 API Gateway — cross-cutting configuration.
 *
 * CORS is configured exclusively via application.yml (spring.cloud.gateway.globalcors).
 * A Java CorsWebFilter bean must NOT be defined here — if both the yml globalcors and
 * a CorsWebFilter bean are active simultaneously, the gateway adds the
 * Access-Control-Allow-Origin header twice, which browsers reject with a
 * "multiple values" error.
 *
 * Routes are declared in application.yml.
 */
@Configuration
public class GatewayConfig {
    // No beans needed here — all config lives in application.yml
}
