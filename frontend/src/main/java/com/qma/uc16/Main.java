package com.qma.uc16;

import com.qma.uc16.config.DatabaseConfig;
import com.qma.uc16.model.Measurement;
import com.qma.uc16.repository.JdbcMeasurementRepository;
import com.qma.uc16.service.MeasurementService;
import java.util.List;

/**
 * UC16 - JDBC Database Integration
 * H2 in-memory database, JDBC with parameterized queries.
 * Run: mvn exec:java -Dexec.mainClass=com.qma.uc16.Main
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC16: JDBC Database Integration         ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Initialize schema
        DatabaseConfig.initializeSchema();

        MeasurementService service = new MeasurementService(new JdbcMeasurementRepository());

        System.out.println("\n  ── INSERT ──");
        Measurement m1 = service.save(1.0,   "ft",  "LENGTH");
        Measurement m2 = service.save(500.0,  "g",   "WEIGHT");
        Measurement m3 = service.save(2.5,   "l",   "VOLUME");
        Measurement m4 = service.save(36.0,  "in",  "LENGTH");
        System.out.println("  Saved: " + m1);
        System.out.println("  Saved: " + m2);
        System.out.println("  Saved: " + m3);

        System.out.println("\n  ── SELECT ALL ──");
        List<Measurement> all = service.findAll();
        all.forEach(m -> System.out.println("  " + m));

        System.out.println("\n  ── SELECT BY CATEGORY: LENGTH ──");
        service.findByCategory("LENGTH").forEach(m -> System.out.println("  " + m));

        System.out.println("\n  ── SELECT BY ID ──");
        service.findById(m2.getId()).ifPresent(m -> System.out.println("  " + m));

        System.out.println("\n  ── DELETE ──");
        boolean deleted = service.delete(m3.getId());
        System.out.println("  Deleted id=" + m3.getId() + ": " + deleted);

        System.out.println("\n  ── FINAL STATE ──");
        service.findAll().forEach(m -> System.out.println("  " + m));

        System.out.println("\n✔  UC16 JDBC integration complete!");
    }
}
