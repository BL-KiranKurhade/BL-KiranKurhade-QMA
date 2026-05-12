package com.qma.measurement.model;
import jakarta.persistence.*;

@Entity @Table(name="measurements")
public class Measurement {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private double value;
    private String unit;
    private String category;

    public Measurement() {}
    public Measurement(double value, String unit, String category) {
        this.value=value; this.unit=unit; this.category=category.toUpperCase();
    }
    public Long getId()         { return id; }
    public double getValue()    { return value; }
    public String getUnit()     { return unit; }
    public String getCategory() { return category; }
    public void setValue(double v)   { this.value=v; }
    public void setUnit(String u)    { this.unit=u; }
    public void setCategory(String c){ this.category=c.toUpperCase(); }
}
