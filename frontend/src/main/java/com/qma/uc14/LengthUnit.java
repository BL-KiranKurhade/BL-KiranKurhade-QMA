package com.qma.uc14;
public enum LengthUnit implements IUnit {
    INCH(1.0,"in"),FEET(12.0,"ft");
    private final double f;private final String s;
    LengthUnit(double f,String s){this.f=f;this.s=s;}
    @Override public double getBaseUnitFactor(){return f;}
    @Override public String getSymbol(){return s;}
    @Override public String getCategory(){return "LENGTH";}
}
