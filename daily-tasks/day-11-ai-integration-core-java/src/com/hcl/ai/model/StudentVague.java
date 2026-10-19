package com.hcl.ai.model;

/**
 * Output from VAGUE PROMPT: "Create a Student class in Java".
 * Notice the severe design anti-patterns commonly produced by unconstrained AI models:
 * 1. Public mutable fields violating encapsulation
 * 2. No argument validation (negative age, null names allowed)
 * 3. Deliberately missing equals() and hashCode()
 * 4. Missing toString() and constructor flexibility
 */
public class StudentVague {
    public int id;
    public String name;
    public String email;
    public double gpa;
}
