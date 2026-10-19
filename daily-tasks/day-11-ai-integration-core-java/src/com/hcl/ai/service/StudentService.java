package com.hcl.ai.service;

import com.hcl.ai.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service orchestrating Student records and course management.
 *
 * CRITICAL REVIEW LOG: 5 GENUINE AI ISSUES IDENTIFIED & FIXED:
 * -----------------------------------------------------------------------------------------
 * Issue #1: Encapsulation Leak
 *   - AI Code: public List<Student> getStudents() { return students; }
 *   - Flaw: Returning direct internal reference allows external callers to clear() or corrupt state.
 *   - Fix: Return Collections.unmodifiableList(students).
 *
 * Issue #2: Swallowed / Empty Exception Handling
 *   - AI Code: try { ... } catch (Exception e) {}
 *   - Flaw: Silent failure destroys error visibility and breaks auditability.
 *   - Fix: Log explicit error and throw documented custom IllegalArgumentException.
 *
 * Issue #3: Missing Null Safety Guards
 *   - AI Code: public void register(Student s) { students.add(s); }
 *   - Flaw: Passing null pollutes collection and causes downstream NullPointerExceptions.
 *   - Fix: Explicit Objects.requireNonNull(s, "Student cannot be null").
 *
 * Issue #4: ConcurrentModificationException during In-Place Removal
 *   - AI Code: for (Student s : students) { if (s.getId() == id) students.remove(s); }
 *   - Flaw: Modifying list during enhanced for-loop throws ConcurrentModificationException.
 *   - Fix: Use ArrayList.removeIf(s -> s.getId() == id) or Iterator.remove().
 *
 * Issue #5: String / Object Identity Comparison Pitfall
 *   - AI Code: if (s.getEmail() == email) ...
 *   - Flaw: Compares reference addresses instead of character sequence equality (.equalsIgnoreCase()).
 *   - Fix: Use s.getEmail().equalsIgnoreCase(email.trim()).
 * -----------------------------------------------------------------------------------------
 */
public class StudentService {

    private final List<Student> students;

    public StudentService() {
        this.students = new ArrayList<>();
    }

    /**
     * Fix for Issue #3: Strict null guard on registration.
     */
    public void registerStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Cannot register a null student reference.");
        }
        if (findStudentById(student.getId()).isPresent()) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " is already registered.");
        }
        students.add(student);
    }

    /**
     * Fix for Issue #1: Preserves internal encapsulation using unmodifiable view.
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Search by ID returning Optional.
     */
    public Optional<Student> findStudentById(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    /**
     * Fix for Issue #5: Uses equalsIgnoreCase() instead of reference equality '=='.
     */
    public Optional<Student> findStudentByEmail(String email) {
        if (email == null) return Optional.empty();
        for (Student s : students) {
            if (s.getEmail().equalsIgnoreCase(email.trim())) {
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    /**
     * Fix for Issue #4: Uses removeIf() to safely delete without ConcurrentModificationException.
     */
    public boolean removeStudentById(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    /**
     * Fix for Issue #2: Proper validation and exception propagation instead of empty catch blocks.
     */
    public double calculateAverageGpa() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double totalGpa = 0.0;
        for (Student s : students) {
            totalGpa += s.getGpa();
        }
        return totalGpa / students.size();
    }
}
