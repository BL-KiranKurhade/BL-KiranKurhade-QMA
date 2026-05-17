package com.qma.uc18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QmaSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(QmaSecurityApplication.class, args);
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.println("║  QMA Security App running on port 8085       ║");
        System.out.println("║  Login: POST /api/auth/login                 ║");
        System.out.println("║  OAuth2: GET /oauth2/authorization/google    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
