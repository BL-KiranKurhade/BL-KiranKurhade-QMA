package com.qma.uc17.model;

//import javax.persistence.*;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/**
 * JPA Entity mapped to 'measurements' table.
 * Spring JPA / Hibernate manages schema creation.
 */
@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(nullable = false, length = 20)
    private String category;

    protected Measurement() {}

    public Measurement(double value, String unit, String category) {
        this.value = value; this.unit = unit; this.category = category.toUpperCase();
    }

    public Long getId()         { return id; }
    public double getValue()    { return value; }
    public void setValue(double v) { this.value = v; }
    public String getUnit()     { return unit; }
    public void setUnit(String u) { this.unit = u; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c.toUpperCase(); }
}
