# Day 18: Relational Database Concepts — Normalization to 3NF & ER Modeling

## Objective
Take a de-normalized, flat `college_registrations` spreadsheet containing student, course, faculty, registration, and fee payment data, analyze its relational anomalies (insertion, deletion, update), and normalize it step-by-step to **Third Normal Form (3NF)**. Provide the comprehensive ER diagram and executable SQL DDL schema.

---

## Normalization Summary

```
UNNORMALIZED SPREADSHEET (UNF)
        │
        ▼ (Ensure atomic values, remove repeating groups, establish PK)
FIRST NORMAL FORM (1NF)
        │
        ▼ (Eliminate Partial Functional Dependencies on composite keys)
SECOND NORMAL FORM (2NF)
        │  ├── Students
        │  ├── Courses
        │  ├── Faculty
        │  └── Course Offerings & Registrations
        │
        ▼ (Eliminate Transitive Functional Dependencies: dept_name from course/faculty)
THIRD NORMAL FORM (3NF)
        ├── departments
        ├── faculty
        ├── courses
        ├── students
        ├── course_offerings
        ├── registrations
        └── fee_payments
```

---

## Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    DEPARTMENTS ||--o{ FACULTY : employs
    DEPARTMENTS ||--o{ COURSES : offers
    COURSES ||--o{ COURSE_OFFERINGS : schedules
    FACULTY ||--o{ COURSE_OFFERINGS : teaches
    STUDENTS ||--o{ REGISTRATIONS : enrolls
    COURSE_OFFERINGS ||--o{ REGISTRATIONS : receives
    REGISTRATIONS ||--o{ FEE_PAYMENTS : incurs

    DEPARTMENTS {
        int department_id PK
        string department_code UK
        string department_name
        string office_building
    }

    FACULTY {
        int faculty_id PK
        int department_id FK
        string full_name
        string email UK
        string office_room
        string phone
    }

    COURSES {
        int course_id PK
        string course_code UK
        int department_id FK
        string course_title
        tinyint credits
    }

    STUDENTS {
        int student_id PK
        string student_number UK
        string first_name
        string last_name
        string email UK
        string phone
        string residential_address
        date enrolled_date
    }

    COURSE_OFFERINGS {
        int offering_id PK
        int course_id FK
        int faculty_id FK
        enum semester
        year academic_year
        int capacity
    }

    REGISTRATIONS {
        int registration_id PK
        int student_id FK
        int offering_id FK
        date registration_date
        string grade
    }

    FEE_PAYMENTS {
        int payment_id PK
        int registration_id FK
        decimal amount
        enum payment_status
        date payment_date
        string transaction_reference UK
    }
```

---

## Directory Structure

```
day-18-relational-database-concepts/
├── normalization-worksheet.md      # Detailed step-by-step breakdown & dependency analysis
├── schema-3nf.sql                  # Production-ready MySQL DDL with constraints & seed data
└── README.md                       # Comprehensive guide and Mermaid ER model
```

---

## Relational Anomalies Resolved

1. **Insertion Anomaly Resolved**:
   - Courses and Faculty can now be created in `courses` and `faculty` tables before any student enrolls in them.
2. **Deletion Anomaly Resolved**:
   - Dropping a student's enrollment record from `registrations` only removes that enrollment; course catalog and faculty records remain completely intact.
3. **Update Anomaly Resolved**:
   - Modifying a department name or faculty room number requires updating exactly one row in `departments` or `faculty`, guaranteeing system-wide consistency.
4. **Data Redundancy Resolved**:
   - Textual strings (course titles, student addresses, emails) are stored exactly once, referenced everywhere else by compact primary key integers.

---

## How to Verify & Execute Schema

Execute in MySQL CLI or MySQL Workbench:
```bash
mysql -u root -p < schema-3nf.sql
```
