# AI Prompting & Review Log (AI-USAGE.md)

This log records the prompts, AI-generated outputs, critical reviews, and architectural remediations performed as part of **Day 11: AI Integration (Core Java) — Prompt & Review Drill**.

---

## 📝 Entry 1: Vague vs. Specific Prompting (Student Entity)

### Prompt A (Vague Prompt)
> *"Write a Student class in Java with id, name, email, and gpa."*

#### AI-Generated Output:
```java
public class Student {
    public int id;
    public String name;
    public String email;
    public double gpa;
}
```

#### Critical Review:
- ❌ **Zero Encapsulation**: All fields declared `public`, allowing unrestricted external modification.
- ❌ **Missing Invariants**: No validation against negative IDs, empty names, malformed emails, or negative/overflowing GPA.
- ❌ **Missing Java Contracts**: Omits `equals()`, `hashCode()`, and `toString()`.

---

### Prompt B (Specific & Constrained Prompt)
> *"Write an immutable, strictly encapsulated Java Student domain model. Fields: id (positive int), name (non-empty string), email (validated string with @ and .), gpa (double between 0.0 and 4.0), and enrolledCourses (List<String>). Enforce all invariants in constructor, use defensive list copies, provide unmodifiable getters, and implement equals() and hashCode() based on id."*

#### AI-Generated Output:
- Produced complete, robust `Student.java` with immutable fields (`private final`), validation guards throwing `IllegalArgumentException`, `Collections.unmodifiableList()` return, and contract-compliant `equals()` / `hashCode()`.

#### Verdict:
- ✅ Constrained prompt eliminated 100% of the architectural anti-patterns found in the vague response.

---

## 📝 Entry 2: Review of AI-Generated `StudentService`

### Prompt
> *"Create a Java service class called StudentService that stores students in a list, allows registration, retrieval by id and email, removal by id, and GPA calculation."*

#### AI-Generated Code Snippet:
```java
public class StudentService {
    private List<Student> students = new ArrayList<>();

    public void register(Student s) {
        students.add(s); // Missing null check
    }

    public List<Student> getStudents() {
        return students; // Leaks internal mutable list
    }

    public void removeStudent(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                students.remove(s); // Throws ConcurrentModificationException!
            }
        }
    }

    public Student findByEmail(String email) {
        for (Student s : students) {
            if (s.getEmail() == email) { // Bug: Reference equality '==' instead of equals()
                return s;
            }
        }
        return null;
    }
}
```

#### Critical Review & Remediations:
1. **Encapsulation Leak**: Replaced `return students;` with `Collections.unmodifiableList(students)`.
2. **ConcurrentModificationException**: Replaced modifying enhanced-for loop with `students.removeIf(s -> s.getId() == id)`.
3. **Null Safety**: Added parameter validation to prevent null pointer exceptions.
4. **Identity Comparison**: Changed `s.getEmail() == email` to `s.getEmail().equalsIgnoreCase(email.trim())`.

---

## 📝 Entry 3: AI-Generated Exception Handling

### Prompt
> *"Add a method to parse student data from user input strings and register the student, handling any possible errors."*

#### AI-Generated Code Snippet:
```java
public void parseAndRegister(String data) {
    try {
        String[] parts = data.split(",");
        Student s = new Student(Integer.parseInt(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3]), null);
        students.add(s);
    } catch (Exception e) {
        // Ignored
    }
}
```

#### Critical Review:
- ❌ **Swallowed Exception**: Empty catch block completely hides parsing errors, corrupt inputs, or memory allocation failures.
- ❌ **Zero Logging / Feedback**: Callers receive no indication of failure or why an entry was skipped.

#### Remediation:
- Replaced with explicit `IllegalArgumentException` propagation, meaningful error messages, and input length checks prior to parsing.
