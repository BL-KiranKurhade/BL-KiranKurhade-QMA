package com.qma.uc8;

/**
 * UC8 - Refactoring Unit Enum to Standalone
 * Quantity now depends on IUnit interface → Dependency Inversion, SRP achieved.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC8: Refactoring Unit Enum to Standalone║");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.println("\n  IUnit interface decouples Quantity from concrete enums.");
        System.out.println("  LengthUnit implements IUnit independently.\n");

        Quantity q1 = new Quantity(1, LengthUnit.FEET);
        Quantity q2 = new Quantity(12, LengthUnit.INCH);

        System.out.println("  q1 = " + q1 + "  [category: " + q1.getUnit().getCategory() + "]");
        System.out.println("  q2 = " + q2 + "  [category: " + q2.getUnit().getCategory() + "]");

        check("1 ft == 12 in",             q1.equals(q2), true);
        check("Convert 1ft → in = 12",     q1.convertTo(LengthUnit.INCH).getValue() == 12.0, true);
        check("1ft + 1ft in INCH = 24 in", q1.add(q1, LengthUnit.INCH).getValue() == 24.0, true);

        System.out.println("  Cross-unit add: " + q1.add(q2));
       // System.out.println("\n✔  All UC8 tests passed!");
    }

    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n", (r==exp?"PASS":"FAIL"), name);
        if (r != exp) throw new AssertionError("FAILED: " + name);
    }
}
