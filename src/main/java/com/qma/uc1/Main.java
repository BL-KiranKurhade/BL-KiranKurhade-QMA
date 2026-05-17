package com.qma.uc1;

/**
 * UC1 - Feet Measurement Equality
 * Demonstrates: Object Equality, Floating-point Comparison, Null/Type Checking.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC1: Feet Measurement Equality          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        Feet f3 = new Feet(2.0);

        assertTest("1.0 feet == 1.0 feet", f1.equals(f2), true);
        assertTest("1.0 feet != 2.0 feet", !f1.equals(f3), true);
        assertTest("Null check",            !f1.equals(null), true);
        assertTest("Type check (vs String)",!f1.equals("1.0"), true);
        assertTest("Self equality",          f1.equals(f1), true);
        assertTest("Zero equality",          new Feet(0.0).equals(new Feet(0.0)), true);
        assertTest("Negative feet",          new Feet(-3.5).equals(new Feet(-3.5)), true);

        System.out.println("\n✔  All UC1 tests passed!");
    }

    static void assertTest(String name, boolean result, boolean expected) {
        String status = (result == expected) ? "PASS" : "FAIL";
        System.out.printf("  [%s] %s%n", status, name);
        if (result != expected) throw new AssertionError("FAILED: " + name);
    }
}
