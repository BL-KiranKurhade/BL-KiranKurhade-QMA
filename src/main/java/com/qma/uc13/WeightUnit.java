package com.qma.uc13;
public enum WeightUnit implements IUnit {
    GRAM(1.0,"g"),KILOGRAM(1000.0,"kg");
    private final double f;private final String s;
    WeightUnit(double f,String s){this.f=f;this.s=s;}
    @Override public double getBaseUnitFactor(){return f;}
    @Override public String getSymbol(){return s;}
    @Override public String getCategory(){return "WEIGHT";}
}
