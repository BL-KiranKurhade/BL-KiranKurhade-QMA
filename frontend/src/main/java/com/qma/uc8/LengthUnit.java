package com.qma.uc8;

/**
 * Standalone LengthUnit implementing IUnit.
 * Concepts: Circular Dependency Elimination, Cohesion, Java Enum Capabilities,
 * Delegation Pattern, Scalability.
 */
public enum LengthUnit implements IUnit {
    MILLIMETRE(1.0 / 25.4, "mm"),
    CENTIMETRE(1.0 / 2.54, "cm"),
    INCH(1.0,              "in"),
    FEET(12.0,             "ft"),
    YARD(36.0,             "yd");

    private final double factor;
    private final String symbol;

    LengthUnit(double factor, String symbol) {
        this.factor = factor;
        this.symbol = symbol;
    }

    @Override public double getBaseUnitFactor() { return factor; }
    @Override public String getSymbol()          { return symbol; }
    @Override public String getCategory()        { return "LENGTH"; }
}
