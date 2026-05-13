package com.qma.uc4;

/**
 * UC4 - Extended Unit Support
 * Demonstrates ENUM extensibility: adding units without changing Quantity class.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC4: Extended Unit Support              ║");
        System.out.println("╚══════════════════════════════════════════╝");

        check("1 FEET  == 12 INCH",           new Quantity(1,LengthUnit.FEET).equals(new Quantity(12,LengthUnit.INCH)), true);
        check("1 YARD  == 3 FEET",            new Quantity(1,LengthUnit.YARD).equals(new Quantity(3,LengthUnit.FEET)), true);
        check("2.54 CM == 1 INCH",            new Quantity(2.54,LengthUnit.CENTIMETRE).equals(new Quantity(1,LengthUnit.INCH)), true);
        check("25.4 MM == 1 INCH",            new Quantity(25.4,LengthUnit.MILLIMETRE).equals(new Quantity(1,LengthUnit.INCH)), true);
        check("1 YARD  == 36 INCH",           new Quantity(1,LengthUnit.YARD).equals(new Quantity(36,LengthUnit.INCH)), true);

        // Validation
        try { new Quantity(-1, LengthUnit.INCH); System.out.println("  [FAIL] Negative should throw"); }
        catch (IllegalArgumentException e) { System.out.println("  [PASS] Negative value rejected: " + e.getMessage()); }

        System.out.println("\n✔  All UC4 tests passed!");
    }

    static void check(String name, boolean result, boolean expected) {
        System.out.printf("  [%s] %s%n", (result==expected?"PASS":"FAIL"), name);
        if (result != expected) throw new AssertionError("FAILED: " + name);
    }
}
