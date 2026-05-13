package com.qma.uc17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application entry point.
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 */
@SpringBootApplication
public class QmaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QmaApplication.class, args);
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║  QMA Spring Boot Backend running on port 8080        ║");
        System.out.println("║  API: http://localhost:8080/api/measurements          ║");
        System.out.println("║  H2:  http://localhost:8080/h2-console               ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}
