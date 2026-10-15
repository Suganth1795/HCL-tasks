package com.hcl.employee.demo;

import java.util.Objects;

/**
 * Anti-Pattern Demonstration Class: Overrides equals() but FAILS to override hashCode().
 * This violates the Object contract:
 * "If two objects are equal according to the equals(Object) method, then calling the
 *  hashCode method on each of the two objects must produce the same integer result."
 */
public class BrokenKeyEmployee {

    private final int id;
    private final String name;

    public BrokenKeyEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrokenKeyEmployee that = (BrokenKeyEmployee) o;
        return this.id == that.id && Objects.equals(this.name, that.name);
    }

    // Notice: hashCode() is DELIBERATELY OMITTED!
    // Inherits java.lang.Object.hashCode() (system identity hash based on memory address)

    @Override
    public String toString() {
        return "BrokenKeyEmployee{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
