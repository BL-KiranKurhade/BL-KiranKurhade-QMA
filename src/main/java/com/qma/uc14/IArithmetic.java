package com.qma.uc14;

/**
 * Interface Segregation: Arithmetic-capable quantities.
 * Temperature CANNOT meaningfully add (30°C + 30°C ≠ 60°C),
 * so Temperature does NOT implement this.
 */
public interface IArithmetic<T> {
    T add(T other);
    T subtract(T other);
}
