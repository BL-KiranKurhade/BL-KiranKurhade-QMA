package com.qma.conversion.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Persists every conversion request so users can see their history.
 * Table: conversion_history
 */
@Entity
@Table(name = "conversion_history")
public class ConversionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double fromValue;

    @Column(nullable = false, length = 20)
    private String fromUnit;

    @Column(nullable = false, length = 20)
    private String toUnit;

    @Column(nullable = false, length = 20)
    private String category;

    @Column(nullable = false)
    private double result;

    /** Username extracted from JWT token (null if called anonymously). */
    @Column(length = 100)
    private String username;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public ConversionHistory() {}

    public ConversionHistory(double fromValue, String fromUnit, String toUnit,
                             String category, double result, String username) {
        this.fromValue = fromValue;
        this.fromUnit  = fromUnit;
        this.toUnit    = toUnit;
        this.category  = category;
        this.result    = result;
        this.username  = username;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()              { return id; }
    public double getFromValue()     { return fromValue; }
    public void setFromValue(double v){ this.fromValue = v; }
    public String getFromUnit()      { return fromUnit; }
    public void setFromUnit(String s){ this.fromUnit = s; }
    public String getToUnit()        { return toUnit; }
    public void setToUnit(String s)  { this.toUnit = s; }
    public String getCategory()      { return category; }
    public void setCategory(String s){ this.category = s; }
    public double getResult()        { return result; }
    public void setResult(double r)  { this.result = r; }
    public String getUsername()      { return username; }
    public void setUsername(String s){ this.username = s; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
