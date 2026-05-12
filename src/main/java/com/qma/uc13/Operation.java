package com.qma.uc13;

/**
 * Enum-Based Operation Dispatch — each constant carries its lambda.
 * Concepts: Enum as Behavior Carrier, Parametric Polymorphism, Scalability.
 */
public enum Operation {
    ADD      ((a, b) -> a + b),
    SUBTRACT ((a, b) -> a - b),
    MULTIPLY ((a, b) -> a * b),
    DIVIDE   ((a, b) -> {
        if (b == 0) throw new ArithmeticException("Division by zero");
        return a / b;
    });

    private final ArithmeticOperation fn;

    Operation(ArithmeticOperation fn) { this.fn = fn; }

    public double apply(double a, double b) { return fn.apply(a, b); }
}
