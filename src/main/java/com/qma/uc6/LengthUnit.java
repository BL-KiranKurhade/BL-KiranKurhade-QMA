package com.qma.uc6;

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
    public double getToInchFactor() { return toInchFactor; }
    public String getSymbol()        { return symbol; }
}
