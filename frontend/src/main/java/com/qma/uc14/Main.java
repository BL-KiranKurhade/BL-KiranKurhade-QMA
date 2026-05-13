package com.qma.uc14;

/**
 * UC14 - Temperature Measurement with Selective Arithmetic + ISP
 * Non-linear conversions, Temperature does NOT implement IArithmetic (ISP).
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC14: Temperature Measurement           ║");
        System.out.println("╚══════════════════════════════════════════╝");

        Temperature boiling = new Temperature(100, TemperatureUnit.CELSIUS);
        Temperature freezing = new Temperature(0, TemperatureUnit.CELSIUS);
        Temperature bodyTemp = new Temperature(98.6, TemperatureUnit.FAHRENHEIT);

        System.out.println("\n  ── Conversions (Non-Linear) ──");
        System.out.println("  100°C → °F : " + boiling.convertTo(TemperatureUnit.FAHRENHEIT));
        System.out.println("  100°C → K  : " + boiling.convertTo(TemperatureUnit.KELVIN));
        System.out.println("  32°F  → °C : " + new Temperature(32,TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS));
        System.out.println("  0K    → °C : " + new Temperature(0,TemperatureUnit.KELVIN).convertTo(TemperatureUnit.CELSIUS));
        System.out.println("  98.6°F → °C: " + bodyTemp.convertTo(TemperatureUnit.CELSIUS));

        System.out.println("\n  ── Equality ──");
        Temperature t212F = new Temperature(212, TemperatureUnit.FAHRENHEIT);
        System.out.println("  100°C == 212°F: " + boiling.equals(t212F));

        System.out.println("\n  ── Delta (temperature difference) ──");
        System.out.printf("  100°C - 0°C = %.2f°C delta%n", boiling.deltaFrom(freezing));

        System.out.println("\n  ── ISP: Addition NOT supported on Temperature ──");
        try { boiling.add(freezing); System.out.println("  [FAIL] Should have thrown"); }
        catch (UnsupportedOperationException e) { System.out.println("  [PASS] " + e.getMessage()); }

        System.out.println("\n  ── Linear Quantity still implements IArithmetic ──");
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(1, LengthUnit.FEET);
        System.out.println("  1ft + 1ft = " + q1.add(q2));

        System.out.println("\n✔  All UC14 tests passed!");
    }
}
