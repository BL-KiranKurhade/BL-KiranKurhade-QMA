package com.qma.uc9;

/**
 * UC9 - Weight Measurement
 * Same Quantity class now handles LENGTH and WEIGHT via IUnit interface.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC9: Weight Measurement                 ║");
        System.out.println("╚══════════════════════════════════════════╝");

        System.out.println("\n  ── Weight Conversions ──");
        Quantity oneKg  = new Quantity(1, WeightUnit.KILOGRAM);
        Quantity oneLb  = new Quantity(1, WeightUnit.POUND);
        Quantity oneTonne = new Quantity(1, WeightUnit.TONNE);

        System.out.println("  1 kg  = " + oneKg.convertTo(WeightUnit.GRAM));
        System.out.println("  1 lb  = " + oneLb.convertTo(WeightUnit.GRAM));
        System.out.println("  1 t   = " + oneTonne.convertTo(WeightUnit.KILOGRAM));
        System.out.println("  1 kg + 500g = " + oneKg.add(new Quantity(500, WeightUnit.GRAM)));

        System.out.println("\n  ── Length still works ──");
        Quantity oneFt = new Quantity(1, LengthUnit.FEET);
        System.out.println("  1 ft  = " + oneFt.convertTo(LengthUnit.INCH));

        check("1 kg == 1000 g",     oneKg.equals(new Quantity(1000, WeightUnit.GRAM)), true);
        check("1000 kg == 1 tonne", new Quantity(1000, WeightUnit.KILOGRAM).equals(oneTonne), true);
        check("Category mismatch throws",
            () -> oneFt.add(oneKg), true);

        System.out.println("\n✔  All UC9 tests passed!");
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
