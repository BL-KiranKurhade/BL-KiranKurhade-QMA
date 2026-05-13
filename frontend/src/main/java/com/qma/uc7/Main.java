package com.qma.uc7;

/**
 * UC7 - Addition with Target Unit Specification
 * Method overloading: add(other) vs add(other, targetUnit).
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC7: Addition with Target Unit          ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity oneFoot = new Quantity(1, LengthUnit.FEET);
        Quantity oneYard = new Quantity(1, LengthUnit.YARD);

        System.out.println("\n  add(other)            result in unit of first operand:");
        System.out.println("  1 ft + 1 ft   " + oneFoot.add(oneFoot));

        System.out.println("\n  add(other, target)    result in caller-specified unit:");
        System.out.println("  1 ft + 1 ft   INCH: " + oneFoot.add(oneFoot, LengthUnit.INCH));
        System.out.println("  1 ft + 1 ft   CM  : " + oneFoot.add(oneFoot, LengthUnit.CENTIMETRE));
        System.out.println("  1 yd + 1 yd   FEET: " + oneYard.add(oneYard, LengthUnit.FEET));
        System.out.println("  1 yd + 1 ft   INCH: " + oneYard.add(oneFoot, LengthUnit.INCH));

        check("1ft+1ft in INCH == 24", oneFoot.add(oneFoot, LengthUnit.INCH).getValue() == 24.0, true);
        check("1yd+1yd  in FEET == 6", oneYard.add(oneYard, LengthUnit.FEET).getValue() == 6.0,  true);

       // System.out.println("\n✔  All UC7 tests passed!");
    }
    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n", (r==exp?"PASS":"FAIL"), name);
        if (r != exp) throw new AssertionError("FAILED: " + name);
    }
}
