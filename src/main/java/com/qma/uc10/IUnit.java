package com.qma.uc10;

/**
 * IUnit — Enum-as-Behavior-Carrier interface.
 * Concepts: Interface-Based Design, SRP, LSP, OCP, Polymorphism.
 */
public interface IUnit {
    double getBaseUnitFactor();
    String getSymbol();
    String getCategory();
}
