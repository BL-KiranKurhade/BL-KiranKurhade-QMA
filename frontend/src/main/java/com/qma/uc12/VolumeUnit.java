package com.qma.uc12;
public enum VolumeUnit implements IUnit {
    MILLILITRE(1.0,"ml"),LITRE(1000.0,"l"),GALLON(3785.411784,"gal");
    private final double f;private final String s;
    VolumeUnit(double f,String s){this.f=f;this.s=s;}
    @Override public double getBaseUnitFactor(){return f;}
    @Override public String getSymbol(){return s;}
    @Override public String getCategory(){return "VOLUME";}
}
