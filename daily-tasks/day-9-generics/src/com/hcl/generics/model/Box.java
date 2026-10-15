package com.hcl.generics.model;

/**
 * Bounded generic class restricted to subclasses of java.lang.Number.
 * Demonstrates:
 * - Upper bounded type parameter <T extends Number>
 * - Invocation of methods defined on the bound (e.g. doubleValue())
 * - Wildcard comparisons across diverse numeric types (Box<Integer> vs Box<Double>)
 *
 * @param <T> Numeric type bounded by java.lang.Number
 */
public class Box<T extends Number> {

    private final T item;

    public Box(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Boxed item cannot be null.");
        }
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    /**
     * Extracts double precision value using the Number bound contract.
     */
    public double doubleValue() {
        return item.doubleValue();
    }

    /**
     * Wildcard method comparing numeric magnitude with any other Box containing a Number subclass.
     */
    public boolean isGreaterThan(Box<? extends Number> other) {
        if (other == null) return true;
        return this.doubleValue() > other.doubleValue();
    }

    /**
     * Sums the value of this box with another numeric box using wildcards.
     */
    public double sumWith(Box<? extends Number> other) {
        if (other == null) return this.doubleValue();
        return this.doubleValue() + other.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("Box[%s (%s)]", item, item.getClass().getSimpleName());
    }
}
