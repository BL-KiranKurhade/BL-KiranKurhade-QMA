package com.qma.measurement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication @EnableDiscoveryClient
public class MeasurementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeasurementServiceApplication.class, args);
        System.out.println("\n✔ Measurement Service started at http://localhost:8084");
        System.out.println("  Swagger UI : http://localhost:8084/swagger-ui.html");
        System.out.println("  Health     : http://localhost:8084/api/measurements/health");
    }
}
