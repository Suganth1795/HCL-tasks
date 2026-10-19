package com.hcl.modern;

/**
 * Circle record modeling immutable geometric circle.
 */
public record Circle(double radius) implements Shape {
    public Circle {
        if (radius <= 0) {
            throw new IllegalArgumentException("Circle radius must be strictly positive: " + radius);
        }
    }

    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}
