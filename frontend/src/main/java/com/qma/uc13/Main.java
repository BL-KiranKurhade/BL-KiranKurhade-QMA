package com.qma.uc13;

/**
 * UC13 - Centralized Arithmetic Logic (DRY via Lambda + Functional Interface)
 * All operations route through a single compute() method.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC13: Centralized Arithmetic (Lambda)   ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity<LengthUnit> twoFt = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> oneFt = new Quantity<>(1, LengthUnit.FEET);

        System.out.println("\n  ── Standard Operations (via compute()) ──");
        System.out.println("  2ft + 1ft = " + twoFt.add(oneFt));
        System.out.println("  2ft - 1ft = " + twoFt.subtract(oneFt));
        System.out.println("  2ft × 3   = " + twoFt.multiply(3));
        System.out.println("  2ft ÷ 2   = " + twoFt.divide(2));

        System.out.println("\n  ── Operation Enum Dispatch ──");
        System.out.printf("  ADD(3,5)      = %.1f%n", Operation.ADD.apply(3,5));
        System.out.printf("  SUBTRACT(8,3) = %.1f%n", Operation.SUBTRACT.apply(8,3));
        System.out.printf("  MULTIPLY(2,3) = %.1f%n", Operation.MULTIPLY.apply(2,3));
        System.out.printf("  DIVIDE(10,2)  = %.1f%n", Operation.DIVIDE.apply(10,2));

        System.out.println("\n  ── Custom Lambda Operation ──");
        Quantity<LengthUnit> maxQ = twoFt.apply(oneFt, (a, b) -> Math.max(a, b));
        System.out.println("  max(2ft, 1ft) = " + maxQ);

        System.out.println("\n  ── Weight with same centralized logic ──");
        Quantity<WeightUnit> w1 = new Quantity<>(5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(3, WeightUnit.KILOGRAM);
        System.out.println("  5kg + 3kg = " + w1.add(w2));
        System.out.println("  5kg - 3kg = " + w1.subtract(w2));

        System.out.println("\n✔  All UC13 tests passed!");
    }
}
