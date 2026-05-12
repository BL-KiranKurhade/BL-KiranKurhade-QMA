package com.qma.uc16.model;

/** Domain model — maps to DB table. */
public class Measurement {
    private Long id;
    private double value;
    private String unit;
    private String category;

    public Measurement() {}
    public Measurement(Long id, double value, String unit, String category) {
        this.id=id; this.value=value; this.unit=unit; this.category=category;
    }

    public Long getId()         { return id; }
    public void setId(Long id)  { this.id=id; }
    public double getValue()    { return value; }
    public String getUnit()     { return unit; }
    public String getCategory() { return category; }

    @Override public String toString() {
        return String.format("Measurement{id=%d, value=%.2f %s [%s]}", id, value, unit, category);
    }
}
