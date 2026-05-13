package com.qma.uc11;

/**
 * UC11 - Volume Measurement (Litre, Millilitre, Gallon)
 * Demonstrates architectural scalability: VolumeUnit added with zero changes to Quantity.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC11: Volume Measurement                ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Quantity<VolumeUnit> oneLitre  = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> oneGallon = new Quantity<>(1, VolumeUnit.GALLON);
        Quantity<VolumeUnit> fiveHundredMl = new Quantity<>(500, VolumeUnit.MILLILITRE);

        System.out.println("\n  ── Volume Conversions ──");
        System.out.println("  1 litre  → ml   : " + oneLitre.convertTo(VolumeUnit.MILLILITRE));
        System.out.println("  1 gallon → litre : " + oneGallon.convertTo(VolumeUnit.LITRE));
        System.out.println("  1 gallon → ml   : " + oneGallon.convertTo(VolumeUnit.MILLILITRE));
        System.out.println("  1 litre  → gallon: " + oneLitre.convertTo(VolumeUnit.GALLON));

        System.out.println("\n  ── Volume Addition ──");
        System.out.println("  1L + 500ml = " + oneLitre.add(fiveHundredMl));
        System.out.println("  1L + 500ml in ML = " + oneLitre.add(fiveHundredMl, VolumeUnit.MILLILITRE));

        check("1 L == 1000 ml",   oneLitre.equals(new Quantity<>(1000, VolumeUnit.MILLILITRE)), true);
        check("1L + 500ml = 1500ml",
            oneLitre.add(fiveHundredMl).equals(new Quantity<>(1500, VolumeUnit.MILLILITRE)), true);

        System.out.println("\n✔  All UC11 tests passed!");
    }

    static void check(String name, boolean r, boolean exp) {
        System.out.printf("  [%s] %s%n", (r==exp?"PASS":"FAIL"), name);
        if (r != exp) throw new AssertionError("FAILED: " + name);
    }
}
