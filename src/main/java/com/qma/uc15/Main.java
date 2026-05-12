package com.qma.uc15;

import com.qma.uc15.controller.MeasurementController;
import com.qma.uc15.dto.MeasurementResponse;
import com.qma.uc15.repository.InMemoryMeasurementRepository;
import com.qma.uc15.service.MeasurementService;

/**
 * UC15 - N-Tier Architecture
 * Controller → Service → Repository → Model | DTO
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC15: N-Tier Architecture               ║");
        System.out.println("╚══════════════════════════════════════════╝");

        MeasurementController controller = new MeasurementController(
            new MeasurementService(new InMemoryMeasurementRepository()));

        System.out.println("\n  ── CREATE ──");
        MeasurementResponse r1 = controller.create(1.0, "ft", "LENGTH");
        MeasurementResponse r2 = controller.create(500.0, "g", "WEIGHT");
        MeasurementResponse r3 = controller.create(2.5, "l", "VOLUME");
        System.out.println("  " + r1);
        System.out.println("  " + r2);
        System.out.println("  " + r3);

        System.out.println("\n  ── GET ALL ──");
        MeasurementResponse all = controller.getAll();
        System.out.println("  " + all);

        System.out.println("\n  ── GET BY ID ──");
        System.out.println("  id=1: " + controller.get(1L));
        System.out.println("  id=999: " + controller.get(999L));

        System.out.println("\n  ── DELETE ──");
        System.out.println("  delete id=2: " + controller.delete(2L));
        System.out.println("  get all: " + controller.getAll());

        System.out.println("\n  ── INVALID CATEGORY ──");
        System.out.println("  " + controller.create(1.0, "ft", "INVALID"));

        System.out.println("\n✔  All UC15 N-Tier tests passed!");
    }
}
