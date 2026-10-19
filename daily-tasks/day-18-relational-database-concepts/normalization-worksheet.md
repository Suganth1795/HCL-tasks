# Normalization Worksheet: College Registrations Dataset to 3NF

---

## 1. Initial Unnormalized Form (UNF) Dataset

Consider a raw, de-normalized flat spreadsheet export (`college_registrations`) used by the university administration office:

### Raw Spreadsheet Attributes:
`registration_id`, `student_id`, `student_name`, `student_email`, `student_phone`, `student_address`, `course_code`, `course_title`, `course_credits`, `department_code`, `department_name`, `faculty_id`, `faculty_name`, `faculty_email`, `faculty_office`, `semester`, `academic_year`, `grade`, `fee_amount`, `fee_status`, `payment_date`

### Sample Flat Records:

| reg_id | student_id | student_name | student_email | student_phone | student_address | course_code | course_title | credits | dept_code | dept_name | faculty_id | faculty_name | faculty_email | faculty_office | semester | year | grade | fee_amount | fee_status | payment_date |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| R101 | S1001 | Alice Walker | alice@univ.edu | 9876543210 | 12 Park Ave, NY | CS101 | Data Structures | 4 | CS | Computer Science | F201 | Dr. Robert Hill | rhill@univ.edu | Tech-301 | Fall | 2026 | A | 1200.00 | PAID | 2026-08-15 |
| R102 | S1001 | Alice Walker | alice@univ.edu | 9876543210 | 12 Park Ave, NY | CS102 | Algorithms | 4 | CS | Computer Science | F202 | Dr. Jane Smith | jsmith@univ.edu | Tech-304 | Fall | 2026 | B+ | 1200.00 | PAID | 2026-08-15 |
| R103 | S1002 | Brian Adams | badams@univ.edu | 9123456780 | 45 Elm St, NY | CS101 | Data Structures | 4 | CS | Computer Science | F201 | Dr. Robert Hill | rhill@univ.edu | Tech-301 | Fall | 2026 | A- | 1200.00 | PENDING | NULL |
| R104 | S1003 | Cathy Zhang | czhang@univ.edu | 9988776655 | 78 Oak Dr, NY | EC201 | Digital Circuits | 3 | EC | Electronics Eng | F301 | Dr. David Ross | dross@univ.edu | Eng-105 | Fall | 2026 | A | 900.00 | PAID | 2026-08-18 |

---

## 2. Relational Anomalies in UNF

1. **Insertion Anomaly**:
   - A new course (e.g. `CS103: Distributed Systems`) or a new faculty member cannot be added into the database unless at least one student has formally registered for that course.
2. **Deletion Anomaly**:
   - If Cathy Zhang (`S1003`) withdraws from `EC201`, deleting registration `R104` inadvertently erases all institutional information about `EC201`, its credits, department, and faculty member Dr. David Ross.
3. **Update / Modification Anomaly**:
   - If Dr. Robert Hill moves from office `Tech-301` to `Tech-405`, every registration row containing Dr. Hill (`R101`, `R103`, etc.) must be updated. Missing even one row creates conflicting, contradictory data across the database.
4. **Data Redundancy**:
   - Student contact info, course descriptions, and department names are duplicated on every enrollment record, causing ballooning storage and cache invalidation challenges.

---

## 3. Normalization Steps (1NF to 3NF)

### Step 1: First Normal Form (1NF)
**Requirements**:
- All column values must be **atomic** (indivisible). Multi-valued attributes (e.g. lists of phone numbers or multiple courses per cell) must be split into single records.
- Each row must be uniquely identifiable by a Primary Key (or Composite Key).
- No repeating column groups (e.g. `course_1`, `course_2`, `course_3`).

**Evaluation**:
In our dataset, multivalued attributes like address can be standardized or separated, and repeating groups are eliminated by ensuring each registration row represents an atomic pair `(student_id, course_code, semester, academic_year)`.

---

### Step 2: Second Normal Form (2NF)
**Requirements**:
- The relation must be in **1NF**.
- Must eliminate **Partial Functional Dependencies**: Every non-prime attribute must be fully functionally dependent on the *entire* candidate primary key, not just a subset of a composite key.

**Dependency Analysis on Composite Key `(student_id, course_code, semester, academic_year)`**:
- `student_name`, `student_email`, `student_phone`, `student_address` depend *only* on `student_id` (Partial Dependency).
- `course_title`, `course_credits`, `dept_code`, `dept_name` depend *only* on `course_code` (Partial Dependency).
- `grade`, `fee_amount`, `fee_status`, `payment_date` depend on the whole composite key.

**Decomposition to achieve 2NF**:
1. `Students(student_id, student_name, student_email, student_phone, student_address)`
2. `Courses(course_code, course_title, course_credits, dept_code, dept_name)`
3. `Faculty(faculty_id, faculty_name, faculty_email, faculty_office, dept_code, dept_name)`
4. `Registrations(registration_id, student_id, course_code, faculty_id, semester, academic_year, grade, fee_amount, fee_status, payment_date)`

---

### Step 3: Third Normal Form (3NF)
**Requirements**:
- The relations must be in **2NF**.
- Must eliminate **Transitive Dependencies**: If $X \to Y$ and $Y \to Z$ (where $Y$ is not a candidate key and $Z$ is a non-prime attribute), then $Z$ is transitively dependent on $X$ and must be extracted into its own relation.

**Transitive Dependency Identification**:
1. In `Courses`: `course_code -> dept_code` and `dept_code -> dept_name`. Therefore, `dept_name` is transitively dependent on `course_code`.
2. In `Faculty`: `faculty_id -> dept_code` and `dept_code -> dept_name`.
3. In `Registrations`: `registration_id -> (student_id, offering)` and `fee details` (which can have multiple payments, distinct receipts, or fee statuses).

**Decomposition to achieve 3NF**:
1. **`departments`**:
   - `department_id` (PK), `department_code` (UNIQUE), `department_name`
2. **`students`**:
   - `student_id` (PK), `first_name`, `last_name`, `email` (UNIQUE), `phone`, `address`
3. **`faculty`**:
   - `faculty_id` (PK), `department_id` (FK), `faculty_name`, `email` (UNIQUE), `office_room`, `phone`
4. **`courses`**:
   - `course_id` (PK), `course_code` (UNIQUE), `department_id` (FK), `course_title`, `credits`
5. **`course_offerings`** (Term sections assigned to faculty):
   - `offering_id` (PK), `course_id` (FK), `faculty_id` (FK), `semester`, `academic_year`
6. **`registrations`**:
   - `registration_id` (PK), `student_id` (FK), `offering_id` (FK), `registration_date`, `grade`
7. **`fee_payments`**:
   - `payment_id` (PK), `registration_id` (FK), `amount`, `status`, `payment_date`, `transaction_ref`

---

## 4. Verification Against Anomalies

| Operation | Before Normalization (UNF) | After 3NF Normalization |
| :--- | :--- | :--- |
| **Insert Course** | Impossible without registering a student | Insert directly into `courses` table |
| **Insert Faculty** | Impossible without an enrolled course | Insert directly into `faculty` table |
| **Delete Registration** | Lost course and faculty info if last student drops | Only deletes the row in `registrations`; `courses` and `faculty` remain intact |
| **Update Office** | Required updating multiple duplicate rows | Update exactly 1 row in `faculty` table |
| **Storage Overhead** | Massive repetition of strings across records | Clean foreign key integer references |
