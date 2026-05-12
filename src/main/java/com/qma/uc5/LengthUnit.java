package com.qma.uc5;

/**
 * LengthUnit enum with conversion factors.
 * Concepts: Enum with conversion factors, Immutability, Value Object Semantics.
 */
public enum LengthUnit {
    MILLIMETRE(1.0 / 25.4, "mm"),
    CENTIMETRE(1.0 / 2.54, "cm"),
    INCH(1.0,              "in"),
    FEET(12.0,             "ft"),
    YARD(36.0,             "yd");

    private final double toInchFactor;
    private final String symbol;

    LengthUnit(double toInchFactor, String symbol) {
        this.toInchFactor = toInchFactor;
        this.symbol = symbol;
    }

    /** Factor to convert this unit to base unit (INCH). */
    public double getToInchFactor() { return toInchFactor; }

    /** Convert a value from this unit to target unit. */
    public double convertTo(double value, LengthUnit target) {
        double inInches = value * this.toInchFactor;
        return inInches / target.toInchFactor;
    }

    public String getSymbol() { return symbol; }
}
