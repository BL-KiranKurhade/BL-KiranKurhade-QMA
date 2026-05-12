package com.qma.uc13;

/**
 * Functional interface for arithmetic operations on base-unit values.
 * Concepts: Functional Interface, Lambda Expressions, Single Source of Truth.
 */
@FunctionalInterface
public interface ArithmeticOperation {
    /**
     * Apply the operation to two base-unit values.
     * @param a left operand (in base unit)
     * @param b right operand (in base unit)
     * @return result in base unit
     */
    double apply(double a, double b);
}
