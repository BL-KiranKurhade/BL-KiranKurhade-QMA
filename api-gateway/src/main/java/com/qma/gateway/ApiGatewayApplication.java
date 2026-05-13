package com.qma.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * UC21 — API Gateway (Spring Cloud Gateway / WebFlux reactive)
 *
 * Single entry-point for all QMA microservices.
 * Routes (defined in application.yml):
 *   ANY  /api/auth/**     → auth-service       (port 8083)
 *   ANY  /api/convert/**  → conversion-service (port 8082)
 *
 * Aggregated Swagger UI : http://localhost:8080/swagger-ui.html
 * Eureka Dashboard      : http://localhost:8761
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║  QMA API Gateway running on port 8080                ║");
        System.out.println("║  Swagger UI : http://localhost:8080/swagger-ui.html  ║");
        System.out.println("║  Eureka     : http://localhost:8761                  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
