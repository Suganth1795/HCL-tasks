package com.hcl.employee.model;

import java.util.Objects;

/**
 * Domain model representing an Employee.
 * Implements Comparable<Employee> establishing natural ordering based on employee ID.
 * Properly overrides equals() and hashCode() to honor the Java Collections contract.
 */
public class Employee implements Comparable<Employee> {

    private final int id;
    private final String name;
    private final String department;
    private final String designation;
    private final double salary;

    public Employee(int id, String name, String department, String designation, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public double getSalary() {
        return salary;
    }

    /**
     * Natural Ordering: Sorted ascending by employee ID.
     */
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.id, other.id);
    }

    /**
     * Equality Contract: Two employees are equal if their unique employee IDs match.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return this.id == employee.id;
    }

    /**
     * HashCode Contract: Must be consistent with equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("EMP#%-4d | %-18s | %-15s | %-20s | $%,10.2f",
                id, name, department, designation, salary);
    }
}
