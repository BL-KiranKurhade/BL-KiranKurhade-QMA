package com.qma.uc9;

/**
 * Weight measurement units.
 * Concepts: Multiple Measurement Categories, Enum-Based Responsibility,
 * Conversion Factor Precision, Base Unit Normalization.
 * Base unit = GRAM.
 */
public enum WeightUnit implements IUnit {
    GRAM(1.0,           "g"),
    KILOGRAM(1000.0,    "kg"),
    TONNE(1_000_000.0,  "t"),
    POUND(453.59237,    "lb"),
    OUNCE(28.349523,    "oz");

    private final double factor;
    private final String symbol;

    WeightUnit(double factor, String symbol) {
        this.factor = factor;
        this.symbol = symbol;
    }

    @Override public double getBaseUnitFactor() { return factor; }
    @Override public String getSymbol()          { return symbol; }
    @Override public String getCategory()        { return "WEIGHT"; }
}
