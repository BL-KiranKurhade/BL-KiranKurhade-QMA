package com.qma.uc8;

/**
 * Unit interface — Separation of Concerns.
 * Concepts: SRP, Dependency Inversion, Encapsulation of Conversion Logic, Type Safety.
 */

public interface IUnit {
    /** Conversion factor relative to the base unit of this category. */
    double getBaseUnitFactor();
    /** Human-readable symbol (e.g., "ft", "kg"). */
    String getSymbol();
    /** Category name — e.g., "LENGTH", "WEIGHT". Used to prevent cross-category ops. */
    String getCategory();
}
