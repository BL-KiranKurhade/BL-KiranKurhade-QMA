package com.qma.uc17.dto;

/** Data Transfer Object for API requests and responses. */
public class MeasurementDTO {
    private Long id;
    private double value;
    private String unit;
    private String category;

    public MeasurementDTO() {}
    public MeasurementDTO(Long id, double value, String unit, String category) {
        this.id=id; this.value=value; this.unit=unit; this.category=category;
    }

    public Long getId()         { return id; }
    public void setId(Long id)  { this.id = id; }
    public double getValue()    { return value; }
    public void setValue(double v) { this.value = v; }
    public String getUnit()     { return unit; }
    public void setUnit(String u) { this.unit = u; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
}
