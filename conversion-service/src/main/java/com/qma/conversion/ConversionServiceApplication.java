package com.qma.conversion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * UC21 Conversion Microservice entry point.
 *
 * @EnableJpaRepositories + @EntityScan are declared explicitly so that
 * Spring Data JPA scans the correct packages even when spring-boot-starter-webflux
 * is also on the classpath (which can confuse auto-configuration).
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.qma.conversion.repository")
@EntityScan(basePackages = "com.qma.conversion.entity")
public class ConversionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConversionServiceApplication.class, args);
        System.out.println("\n✔ Conversion Service started at http://localhost:8082");
        System.out.println("  Swagger UI : http://localhost:8082/swagger-ui.html");
        System.out.println("  Health     : http://localhost:8082/api/convert/health");
    }
}
