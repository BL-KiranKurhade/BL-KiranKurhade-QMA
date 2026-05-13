package com.qma.uc15.dto;

/** DTO for incoming measurement creation requests. Decouples API from domain model. */
public class MeasurementRequest {
    private final double value;
    private final String unit;
    private final String category;

    public MeasurementRequest(double value, String unit, String category) {
        this.value = value; this.unit = unit; this.category = category;
    }

    public double getValue()    { return value; }
    public String getUnit()     { return unit; }
    public String getCategory() { return category; }
}
