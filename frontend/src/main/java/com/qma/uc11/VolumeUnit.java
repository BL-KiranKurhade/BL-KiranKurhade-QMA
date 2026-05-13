package com.qma.uc11;

/**
 * Volume measurement units.
 * Concepts: Scalability of Generic Design, Pattern Replication Across Categories,
 * Base Unit Selection (MILLILITRE), Conversion Factor Precision,
 * Enum as Polymorphic Carrier, Floating-Point Precision.
 * Base unit = MILLILITRE.
 */
public enum VolumeUnit implements IUnit {
    MILLILITRE(1.0,       "ml"),
    LITRE(1000.0,         "l"),
    GALLON(3785.411784,   "gal"),
    CUP(236.5882365,      "cup"),
    TABLESPOON(14.7868,   "tbsp");

    private final double factor;
    private final String symbol;

    VolumeUnit(double factor, String symbol) {
        this.factor = factor;
        this.symbol = symbol;
    }

    @Override public double getBaseUnitFactor() { return factor; }
    @Override public String getSymbol()          { return symbol; }
    @Override public String getCategory()        { return "VOLUME"; }
}
