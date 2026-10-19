package com.hcl.modern;

/**
 * Day 17: Sealed Interface permitting only specific records.
 * Exhaustiveness is enforced by the compiler for pattern-matching switch expressions.
 */
public sealed interface Shape permits Circle, Rectangle {
    double calculateArea();
}
