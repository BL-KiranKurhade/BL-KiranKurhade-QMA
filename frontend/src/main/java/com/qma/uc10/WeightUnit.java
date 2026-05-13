package com.qma.uc10;

public enum WeightUnit implements IUnit {
    GRAM(1.0,"g"), KILOGRAM(1000.0,"kg"), TONNE(1_000_000.0,"t"),
    POUND(453.59237,"lb"), OUNCE(28.349523,"oz");
    private final double f; private final String s;
    WeightUnit(double f, String s){this.f=f;this.s=s;}
    @Override public double getBaseUnitFactor(){return f;}
    @Override public String getSymbol(){return s;}
    @Override public String getCategory(){return "WEIGHT";}
}
