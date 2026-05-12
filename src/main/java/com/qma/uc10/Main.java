package com.qma.uc10;

/**
 * UC10 - Generic Quantity<T extends IUnit>
 * One class, compile-time type safety for all measurement categories.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC10: Generic Quantity<T extends IUnit> ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Length
        Quantity<LengthUnit> oneFoot  = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> twelveIn = new Quantity<>(12, LengthUnit.INCH);
        System.out.println("\n  ── Length ──");
        System.out.println("  1 ft → in: " + oneFoot.convertTo(LengthUnit.INCH));
        System.out.println("  1 ft == 12 in: " + oneFoot.equals(twelveIn));

        // Weight
        Quantity<WeightUnit> oneKg = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> fiveHundredG = new Quantity<>(500, WeightUnit.GRAM);
        System.out.println("\n  ── Weight ──");
        System.out.println("  1 kg → g  : " + oneKg.convertTo(WeightUnit.GRAM));
        System.out.println("  1 kg + 500g = " + oneKg.add(fiveHundredG));
        System.out.println("  1 kg + 500g in KG = " + oneKg.add(fiveHundredG, WeightUnit.KILOGRAM));

        check("1 ft == 12 in (generic)", oneFoot.equals(twelveIn), true);
        check("1 kg == 1000 g (generic)", oneKg.equals(new Quantity<>(1000, WeightUnit.GRAM)), true);

        // System.out.println("\n✔  All UC10 tests passed!");
    }

    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n", (r==exp?"PASS":"FAIL"), name);
        if (r != exp) throw new AssertionError("FAILED: " + name);
    }
}
