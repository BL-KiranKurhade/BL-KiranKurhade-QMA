package com.qma.uc12;

/**
 * UC12 - Subtraction and Division Operations
 * Complete arithmetic on immutable Quantity objects.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC12: Subtraction & Division            ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity<LengthUnit> threeYards = new Quantity<>(3, LengthUnit.YARD);
        Quantity<LengthUnit> oneFoot    = new Quantity<>(1, LengthUnit.FOOT);

        // Handle FOOT not in enum — use FEET
        Quantity<LengthUnit> twoFeet = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> oneFeetQ = new Quantity<>(1, LengthUnit.FEET);

        System.out.println("\n  ── Length Arithmetic ──");
        System.out.println("  3yd - 1ft in INCH: " + threeYards.subtract(oneFeetQ, LengthUnit.INCH));
        System.out.println("  2ft - 1ft       : " + twoFeet.subtract(oneFeetQ));
        System.out.println("  2ft ÷ 2         : " + twoFeet.divide(2));
        System.out.println("  2ft × 3         : " + twoFeet.multiply(3));

        System.out.println("\n  ── Weight Arithmetic ──");
        Quantity<WeightUnit> fiveKg  = new Quantity<>(5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> twoKg   = new Quantity<>(2, WeightUnit.KILOGRAM);
        System.out.println("  5kg - 2kg = " + fiveKg.subtract(twoKg));
        System.out.println("  5kg ÷ 5   = " + fiveKg.divide(5));

        System.out.println("\n  ── Non-Commutativity ──");
        System.out.printf("  5kg - 2kg = %.4f kg%n", fiveKg.subtract(twoKg).getValue());
        System.out.printf("  2kg - 5kg = %.4f kg  (negative result)%n", twoKg.subtract(fiveKg).getValue());

        check("Division by zero throws",
            () -> fiveKg.divide(0), true);
        System.out.println("\n✔  All UC12 tests passed!");
    }

    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n",(r==exp?"PASS":"FAIL"),name);
        if(r!=exp) throw new AssertionError("FAILED: "+name);
    }
    static void check(String name, Runnable code, boolean shouldThrow) {
        try{code.run();System.out.printf("  [%s] %s%n",shouldThrow?"FAIL":"PASS",name);}
        catch(Exception e){System.out.printf("  [%s] %s%n",shouldThrow?"PASS":"FAIL",name);}
    }
}
