package com.hcl.modern;

/**
 * Rectangle record modeling immutable geometric rectangle with square detection.
 */
public record Rectangle(double width, double height) implements Shape {
    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Rectangle dimensions must be strictly positive: " + width + "x" + height);
        }
    }

    @Override
    public double calculateArea() {
        return width * height;
    }

    public boolean isSquare() {
        return Double.compare(width, height) == 0;
    }
}
