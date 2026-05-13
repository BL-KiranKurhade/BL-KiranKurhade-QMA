package com.qma.uc5;

/**
 * UC5 - Unit-to-Unit Conversion
 * Immutable Quantity with convertTo() using Enum conversion factors.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC5: Unit-to-Unit Conversion            ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity oneFoot  = new Quantity(1, LengthUnit.FEET);
        Quantity oneInch  = new Quantity(1, LengthUnit.INCH);
        Quantity oneYard  = new Quantity(1, LengthUnit.YARD);

        System.out.println("\n  Conversions:");
        System.out.printf("  1 ft → in : %s%n", oneFoot.convertTo(LengthUnit.INCH));
        System.out.printf("  1 ft → cm : %s%n", oneFoot.convertTo(LengthUnit.CENTIMETRE));
        System.out.printf("  1 ft → mm : %s%n", oneFoot.convertTo(LengthUnit.MILLIMETRE));
        System.out.printf("  1 ft → yd : %s%n", oneFoot.convertTo(LengthUnit.YARD));
        System.out.printf("  1 in → cm : %s%n", oneInch.convertTo(LengthUnit.CENTIMETRE));
        System.out.printf("  1 yd → ft : %s%n", oneYard.convertTo(LengthUnit.FEET));

        System.out.println("\n  Immutability check:");
        System.out.println("  Before: " + oneFoot);
        oneFoot.convertTo(LengthUnit.INCH);
        System.out.println("  After convertTo (original unchanged): " + oneFoot);

        check("1 ft == 12 in (equality)", oneFoot.equals(new Quantity(12, LengthUnit.INCH)), true);
        check("Null unit throws",
            () -> new Quantity(1, null), true);

        // System.out.println("\n✔  All UC5 tests passed!");
    }

    static void check(String name, boolean result, boolean expected) {
        System.out.printf("  [%s] %s%n", (result==expected?"PASS":"FAIL"), name);
        if (result != expected) throw new AssertionError("FAILED: " + name);
    }

    static void check(String name, Runnable code, boolean shouldThrow) {
        try { code.run(); System.out.printf("  [%s] %s%n", shouldThrow?"FAIL":"PASS", name); }
        catch (Exception e) { System.out.printf("  [%s] %s: %s%n", shouldThrow?"PASS":"FAIL", name, e.getMessage()); }
    }
}
