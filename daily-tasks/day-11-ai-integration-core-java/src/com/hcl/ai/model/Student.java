package com.hcl.ai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Output from SPECIFIC & CONSTRAINED PROMPT:
 * "Create an immutable, strictly encapsulated Student domain entity with
 *  input validation, defensive list copying, valid email regex verification,
 *  and proper equals() and hashCode() conforming to the Java contract."
 */
public final class Student {

    private final int id;
    private final String name;
    private final String email;
    private final double gpa;
    private final List<String> enrolledCourses;

    public Student(int id, String name, String email, double gpa, List<String> courses) {
        if (id <= 0) {
            throw new IllegalArgumentException("Student ID must be positive.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or empty.");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        if (gpa < 0.0 || gpa > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0. Received: " + gpa);
        }

        this.id = id;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.gpa = gpa;
        // Defensive copy to prevent external mutation
        this.enrolledCourses = courses != null ? new ArrayList<>(courses) : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getGpa() {
        return gpa;
    }

    /**
     * Returns an unmodifiable view to preserve encapsulation.
     */
    public List<String> getEnrolledCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%d, Name='%s', Email='%s', GPA=%.2f, Courses=%s]",
                id, name, email, gpa, enrolledCourses);
    }
}
