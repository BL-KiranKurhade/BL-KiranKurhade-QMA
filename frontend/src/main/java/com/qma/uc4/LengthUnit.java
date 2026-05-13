package com.qma.uc4;

/**
 * Extended LengthUnit ENUM — adds MILLIMETRE, CENTIMETRE, YARD.
 * Concepts: ENUM Extensibility, Mathematical Accuracy, Backward Compatibility.
 * Base unit = INCH (all factors convert to inches).
 */
public enum LengthUnit {
    MILLIMETRE(1.0 / 25.4),     // 1 mm = 1/25.4 inches
    CENTIMETRE(1.0 / 2.54),     // 1 cm = 1/2.54 inches
    INCH(1.0),                  // Base unit
    FEET(12.0),                 // 1 ft = 12 inches
    YARD(36.0);                 // 1 yd = 36 inches

    public final double baseUnitFactor;

    LengthUnit(double baseUnitFactor) {
        this.baseUnitFactor = baseUnitFactor;
    }
}
