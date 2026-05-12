package com.qma.uc6;

/**
 * UC6 - Addition of Two Length Units
 * Both operands normalized to base unit → added → result in unit of first operand.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC6: Addition of Two Length Units       ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity q1 = new Quantity(1, LengthUnit.FEET);
        Quantity q2 = new Quantity(12, LengthUnit.INCH);
        Quantity q3 = new Quantity(1, LengthUnit.YARD);

        System.out.println("\n  Additions:");
        System.out.println("  1 ft + 1 ft  = " + q1.add(q1));
        System.out.println("  1 ft + 12 in = " + q1.add(q2) + "  (cross-unit)");
        System.out.println("  1 yd + 1 ft  = " + q3.add(q1));
        System.out.println("  1 in + 1 ft  = " + new Quantity(1, LengthUnit.INCH).add(q1));

        System.out.println("\n  Commutativity:");
        Quantity a = new Quantity(1, LengthUnit.FEET);
        Quantity b = new Quantity(6, LengthUnit.INCH);
        System.out.printf("  a+b base inches: %.4f%n", a.add(b).getValue() * LengthUnit.FEET.getToInchFactor());
        System.out.printf("  b+a base inches: %.4f%n", b.add(a).getValue() * LengthUnit.INCH.getToInchFactor());

        check("1 ft + 12 in == 2 ft", q1.add(q2).equals(new Quantity(2, LengthUnit.FEET)), true);
        check("Null throws",
              () -> q1.add(null), true);

      //  System.out.println("\n✔  All UC6 tests passed!");
    }

    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n", (r==exp?"PASS":"FAIL"), name);
        if (r != exp) throw new AssertionError("FAILED: " + name);
    }
    static void check(String name, Runnable code, boolean shouldThrow) {
        try { code.run(); System.out.printf("  [%s] %s%n", shouldThrow?"FAIL":"PASS", name); }
        catch (Exception e) { System.out.printf("  [%s] %s%n", shouldThrow?"PASS":"FAIL", name); }
    }
}
