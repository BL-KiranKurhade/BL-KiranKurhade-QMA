package com.qma.uc3;

/**
 * UC3 - Generic Quantity Class for DRY Principle
 * One Quantity class handles all length units via ENUM + base-unit conversion.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC3: Generic Quantity Class (DRY)       ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity q1 = new Quantity(1, LengthUnit.FEET);
        Quantity q2 = new Quantity(12, LengthUnit.INCH);
        Quantity q3 = new Quantity(2, LengthUnit.FEET);

        check("1 FEET == 12 INCH (cross-unit)",  q1.equals(q2), true);
        check("12 INCH == 1 FEET (symmetric)",   q2.equals(q1), true);
        check("1 FEET != 2 FEET",                !q1.equals(q3), true);
        check("1 INCH != 1 FEET",
              !new Quantity(1,LengthUnit.INCH).equals(new Quantity(1,LengthUnit.FEET)), true);

        System.out.println("\n  DRY: single equals() logic handles all units!");
       // System.out.println("\n✔  All UC3 tests passed!");
    }

    static void check(String name, boolean result, boolean expected) {
        System.out.printf("  [%s] %s%n", (result==expected?"PASS":"FAIL"), name);
        if (result != expected) throw new AssertionError("FAILED: " + name);
    }
}
