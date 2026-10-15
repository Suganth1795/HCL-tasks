package com.hcl.employee;

import com.hcl.employee.demo.BrokenKeyEmployee;
import com.hcl.employee.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Daily Task 8: Collections Framework - Part 2
 * Topic: Employee Directory
 *
 * Requirements:
 * 1. HashMap lookup by employee ID (O(1) average time complexity)
 * 2. Frequency count with Map.merge()
 * 3. TreeMap department report (naturally sorted keys)
 * 4. Comparable by id (natural ordering)
 * 5. Top-5 salary Comparator chain (reversed salary, then name, then id)
 * 6. Missing-hashCode bug demonstration and fix
 */
public class EmployeeDirectory {

    // 1. HashMap for instant O(1) primary key lookups
    private final Map<Integer, Employee> employeeById;

    // 2. Frequency count of employees per department using Map.merge()
    private final Map<String, Integer> departmentHeadcounts;

    // Raw list of all employee records
    private final List<Employee> allEmployees;

    public EmployeeDirectory() {
        this.employeeById = new HashMap<>();
        this.departmentHeadcounts = new HashMap<>();
        this.allEmployees = new ArrayList<>();
    }

    /**
     * Adds an employee to the directory, indexing in HashMap and incrementing
     * department frequency counter atomically using Map.merge().
     */
    public void addEmployee(Employee emp) {
        employeeById.put(emp.getId(), emp);
        allEmployees.add(emp);

        // Feature 2: Frequency count with Map.merge()
        // If key doesn't exist, associates with 1. If exists, applies Integer::sum
        departmentHeadcounts.merge(emp.getDepartment(), 1, Integer::sum);
    }

    /**
     * Feature 1: Instant O(1) lookup by Employee ID via HashMap.
     */
    public Employee findById(int id) {
        return employeeById.get(id);
    }

    public Map<String, Integer> getDepartmentHeadcounts() {
        return Collections.unmodifiableMap(departmentHeadcounts);
    }

    /**
     * Feature 3: TreeMap Department Report.
     * Groups employees into a TreeMap where keys (department names) are automatically
     * sorted in natural alphabetical order.
     */
    public Map<String, List<Employee>> generateDepartmentReport() {
        Map<String, List<Employee>> report = new TreeMap<>(); // Sorted alphabetically by department
        for (Employee emp : allEmployees) {
            report.computeIfAbsent(emp.getDepartment(), k -> new ArrayList<>()).add(emp);
        }
        return report;
    }

    /**
     * Feature 4: Natural Ordering via Comparable<Employee> (by ID ascending).
     */
    public List<Employee> getEmployeesSortedById() {
        List<Employee> sorted = new ArrayList<>(allEmployees);
        Collections.sort(sorted); // Relies on Employee.compareTo()
        return sorted;
    }

    /**
     * Feature 5: Top-5 Highest Earners using Comparator Chain.
     * Chaining: Primary = Salary (descending), Secondary = Name (ascending), Tertiary = ID (ascending).
     */
    public List<Employee> getTopEarners(int limit) {
        Comparator<Employee> topEarnerComparator = Comparator
                .comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName)
                .thenComparingInt(Employee::getId);

        List<Employee> sorted = new ArrayList<>(allEmployees);
        sorted.sort(topEarnerComparator);

        int count = Math.min(limit, sorted.size());
        return sorted.subList(0, count);
    }

    /**
     * Feature 6: Missing-hashCode Bug Demonstration & Fix Walkthrough.
     */
    public static void demonstrateMissingHashCodeBug() {
        System.out.println("========================================================================");
        System.out.println("       FEATURE 6: MISSING-HASHCODE BUG DEMO AND FIX VERIFICATION        ");
        System.out.println("========================================================================");

        // STEP 1: DEMONSTRATE THE BUG WITH BrokenKeyEmployee
        System.out.println("\n--- [PART A: THE BUG (BrokenKeyEmployee)] ---");
        BrokenKeyEmployee broken1 = new BrokenKeyEmployee(101, "Alice Walker");
        BrokenKeyEmployee broken2 = new BrokenKeyEmployee(101, "Alice Walker");

        System.out.println("Object 1: " + broken1);
        System.out.println("Object 2: " + broken2);
        System.out.println("broken1.equals(broken2) : " + broken1.equals(broken2) + " (Objects are conceptually equal!)");
        System.out.println("System.identityHashCode(broken1): " + System.identityHashCode(broken1));
        System.out.println("System.identityHashCode(broken2): " + System.identityHashCode(broken2));
        System.out.println("broken1.hashCode() == broken2.hashCode(): " + (broken1.hashCode() == broken2.hashCode()));

        Map<BrokenKeyEmployee, String> brokenMap = new HashMap<>();
        brokenMap.put(broken1, "CONFIDENTIAL_PAYROLL_DATA_ALICE");

        String retrievedWithBroken2 = brokenMap.get(broken2);
        System.out.println("Attempting lookup with broken2 key: brokenMap.get(broken2) -> " + retrievedWithBroken2);

        if (retrievedWithBroken2 == null) {
            System.out.println("🚨 BUG CONFIRMED: HashMap returned null even though keys are equal according to equals()!");
            System.out.println("   Root Cause: BrokenKeyEmployee inherited Object.hashCode(), generating different hash buckets.");
        }

        // STEP 2: DEMONSTRATE THE FIX WITH Employee
        System.out.println("\n--- [PART B: THE FIX (Employee with overridden hashCode)] ---");
        Employee valid1 = new Employee(101, "Alice Walker", "Engineering", "Principal Architect", 165000.0);
        Employee valid2 = new Employee(101, "Alice Walker", "Engineering", "Principal Architect", 165000.0);

        System.out.println("valid1.equals(valid2) : " + valid1.equals(valid2));
        System.out.println("valid1.hashCode()     : " + valid1.hashCode());
        System.out.println("valid2.hashCode()     : " + valid2.hashCode());
        System.out.println("valid1.hashCode() == valid2.hashCode(): " + (valid1.hashCode() == valid2.hashCode()));

        Map<Employee, String> validMap = new HashMap<>();
        validMap.put(valid1, "CONFIDENTIAL_PAYROLL_DATA_ALICE");

        String retrievedWithValid2 = validMap.get(valid2);
        System.out.println("Attempting lookup with valid2 key: validMap.get(valid2) -> \"" + retrievedWithValid2 + "\"");

        if ("CONFIDENTIAL_PAYROLL_DATA_ALICE".equals(retrievedWithValid2)) {
            System.out.println("✅ FIX VERIFIED: Consistent hashCode() hashes to the identical bucket, allowing successful retrieval!");
        }
        System.out.println("========================================================================\n");
    }

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("       DAILY TASK 8: COLLECTIONS FRAMEWORK - EMPLOYEE DIRECTORY         ");
        System.out.println("========================================================================\n");

        EmployeeDirectory directory = new EmployeeDirectory();

        // Populate sample employees
        directory.addEmployee(new Employee(105, "Sarah Connor", "Engineering", "Lead DevOps Engineer", 145000.0));
        directory.addEmployee(new Employee(101, "Alice Walker", "Engineering", "Principal Architect", 175000.0));
        directory.addEmployee(new Employee(108, "Bob Martin", "Engineering", "Staff Software Engineer", 155000.0));
        directory.addEmployee(new Employee(102, "David Miller", "Finance", "Senior Financial Analyst", 110000.0));
        directory.addEmployee(new Employee(106, "Emma Watson", "Finance", "VP of Finance", 185000.0));
        directory.addEmployee(new Employee(103, "Grace Hopper", "Research", "Chief Research Scientist", 195000.0));
        directory.addEmployee(new Employee(109, "Alan Turing", "Research", "AI Research Fellow", 190000.0));
        directory.addEmployee(new Employee(104, "John Doe", "Human Resources", "HR Generalist", 85000.0));
        directory.addEmployee(new Employee(107, "Clara Oswald", "Human Resources", "HR Director", 130000.0));
        directory.addEmployee(new Employee(110, "James Bond", "Security", "Head of Information Security", 160000.0));

        // 1. HashMap O(1) Lookup
        System.out.println("--- 1. HASHMAP O(1) LOOKUP BY ID ---");
        int searchId = 103;
        Employee found = directory.findById(searchId);
        System.out.printf("Lookup ID %d: %s\n", searchId, found);

        // 2. Frequency Count with Map.merge()
        System.out.println("\n--- 2. FREQUENCY COUNT PER DEPARTMENT (Map.merge) ---");
        for (Map.Entry<String, Integer> entry : directory.getDepartmentHeadcounts().entrySet()) {
            System.out.printf("  Department: %-18s | Headcount: %d\n", entry.getKey(), entry.getValue());
        }

        // 3. TreeMap Department Report
        System.out.println("\n--- 3. TREEMAP DEPARTMENT REPORT (Naturally Alphabetical) ---");
        Map<String, List<Employee>> report = directory.generateDepartmentReport();
        for (Map.Entry<String, List<Employee>> entry : report.entrySet()) {
            System.out.println("📁 " + entry.getKey().toUpperCase() + " DEPARTMENT (Headcount: " + entry.getValue().size() + "):");
            double deptTotal = 0;
            for (Employee e : entry.getValue()) {
                System.out.println("    • " + e);
                deptTotal += e.getSalary();
            }
            System.out.printf("    Total Department Payroll: $%,.2f | Average: $%,.2f\n\n",
                    deptTotal, deptTotal / entry.getValue().size());
        }

        // 4. Comparable by ID (Natural Ordering)
        System.out.println("--- 4. COMPARABLE BY ID (Natural Ascending Order) ---");
        List<Employee> sortedById = directory.getEmployeesSortedById();
        for (Employee e : sortedById) {
            System.out.println("  " + e);
        }

        // 5. Top-5 Salary Comparator Chain
        System.out.println("\n--- 5. TOP-5 SALARY COMPARATOR CHAIN ---");
        System.out.println("Comparator Chain: Salary (DESC) -> Name (ASC) -> ID (ASC)");
        List<Employee> top5Earners = directory.getTopEarners(5);
        int rank = 1;
        for (Employee e : top5Earners) {
            System.out.printf("  [Rank #%d] %s\n", rank++, e);
        }

        // 6. Missing-hashCode Bug Demo and Fix
        System.out.println();
        demonstrateMissingHashCodeBug();
    }
}
