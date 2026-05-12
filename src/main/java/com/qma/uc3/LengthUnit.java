package com.qma.uc3;

/**
 * ENUM representing length units.
 * Base unit = INCH. All factors convert to inches.
 * Concept: ENUM Usage, Single Responsibility, Scalability.
 */
public enum LengthUnit {
    INCH(1.0),
    FEET(12.0);   // 1 foot = 12 inches

    /** Multiply by this factor to convert to base unit (INCH). */
    public final double baseUnitFactor;

    LengthUnit(double baseUnitFactor) {
        this.baseUnitFactor = baseUnitFactor;
    }
}
