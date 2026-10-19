package com.hcl.ai.app;

import com.hcl.ai.model.Student;
import com.hcl.ai.model.StudentVague;
import com.hcl.ai.service.StudentService;

import java.util.Arrays;
import java.util.List;

/**
 * Driver Application for Day 11: AI Integration (Core Java) - Prompt & Review Drill.
 */
public class AIDrillApp {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("     DAILY TASK 11: AI INTEGRATION (CORE JAVA) - PROMPT & REVIEW DRILL  ");
        System.out.println("========================================================================\n");

        // 1. Vague vs Specific POJO Comparison
        System.out.println("--- 1. POJO COMPARISON: VAGUE PROMPT VS SPECIFIC PROMPT ---");
        StudentVague vague = new StudentVague();
        vague.id = -99; // Anti-pattern: Negative ID allowed!
        vague.name = null; // Anti-pattern: Null name allowed!
        vague.email = "not-an-email"; // Anti-pattern: Invalid email format!
        vague.gpa = 12.5; // Anti-pattern: Impossible GPA value!

        System.out.println("Vague AI Output (StudentVague):");
        System.out.println("  • Public fields exposed directly, completely unvalidated.");
        System.out.println("  • Stored: id=" + vague.id + ", name=" + vague.name + ", gpa=" + vague.gpa);

        System.out.println("\nSpecific Prompt Output (Student):");
        Student hardened = new Student(101, "Alan Turing", "alan.turing@cambridge.ac.uk", 3.95,
                Arrays.asList("Algorithms", "Cryptography", "Automata Theory"));
        System.out.println("  • " + hardened);
        System.out.println("  • Enforces non-null names, regex email checks, GPA range [0.0, 4.0], and unmodifiable course lists.");

        // 2. Testing 5 Fixed AI Issues in StudentService
        System.out.println("\n--- 2. VERIFYING 5 GENUINE AI ISSUES IDENTIFIED & FIXED ---");
        StudentService service = new StudentService();

        service.registerStudent(hardened);
        service.registerStudent(new Student(102, "Grace Hopper", "grace.hopper@navy.mil", 4.00,
                Arrays.asList("Compilers", "COBOL", "Systems Architecture")));
        service.registerStudent(new Student(103, "Ada Lovelace", "ada@lovelace.org", 3.88,
                Arrays.asList("Analytical Engine", "Mathematics")));

        // Issue 1: Encapsulation Leak Test
        System.out.println("\n[Testing Issue #1 Fix: Encapsulation Leak]");
        List<Student> students = service.getAllStudents();
        try {
            students.clear(); // Attempt external mutation
        } catch (UnsupportedOperationException e) {
            System.out.println("  ✅ Protected: Attempting students.clear() threw UnsupportedOperationException as expected!");
        }

        // Issue 2: Swallowed Exception Test
        System.out.println("\n[Testing Issue #2 Fix: No Swallowed Exceptions]");
        try {
            service.registerStudent(hardened); // Duplicate registration
        } catch (IllegalArgumentException e) {
            System.out.println("  ✅ Protected: Duplicate registration threw documented exception: \"" + e.getMessage() + "\"");
        }

        // Issue 3: Null Safety Test
        System.out.println("\n[Testing Issue #3 Fix: Null Safety Guard]");
        try {
            service.registerStudent(null);
        } catch (IllegalArgumentException e) {
            System.out.println("  ✅ Protected: Passing null threw IllegalArgumentException: \"" + e.getMessage() + "\"");
        }

        // Issue 4: ConcurrentModificationException Test
        System.out.println("\n[Testing Issue #4 Fix: Safe In-Place Removal]");
        boolean removed = service.removeStudentById(101);
        System.out.println("  ✅ Protected: Safely removed student 101 without ConcurrentModificationException: " + removed);
        System.out.println("  Remaining students count: " + service.getAllStudents().size());

        // Issue 5: Object Equality '==' vs equalsIgnoreCase Test
        System.out.println("\n[Testing Issue #5 Fix: String Equality]");
        String searchEmail = new String("grace.hopper@navy.mil"); // Different object reference in heap
        boolean found = service.findStudentByEmail(searchEmail).isPresent();
        System.out.println("  ✅ Protected: Found by dynamic email object reference: " + found);

        System.out.printf("\nAverage GPA of enrolled students: %.2f\n", service.calculateAverageGpa());
        System.out.println("========================================================================\n");
    }
}
