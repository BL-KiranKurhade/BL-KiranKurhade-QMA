package com.qma.uc9;

/** Length units. Base: INCH. */
public enum LengthUnit implements IUnit {
    MILLIMETRE(1.0/25.4,"mm"), CENTIMETRE(1.0/2.54,"cm"),
    INCH(1.0,"in"), FEET(12.0,"ft"), YARD(36.0,"yd");

    private final double f; private final String s;
    LengthUnit(double f, String s) { this.f=f; this.s=s; }
    @Override public double getBaseUnitFactor() { return f; }
    @Override public String getSymbol()          { return s; }
    @Override public String getCategory()        { return "LENGTH"; }
}
