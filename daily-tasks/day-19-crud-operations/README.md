# Day 19: Relational Database CRUD Operations & SQL Filtering

## Objective
Establish an enterprise relational database in MySQL containing `departments` and `employees` tables with 32 realistic rows. Answer **12 filtering, sorting, aggregate, and subquery questions**, and demonstrate **safe transactional updates** utilizing `START TRANSACTION`, atomic `UPDATE`, `ROLLBACK`, and `COMMIT`.

---

## Directory Structure

```
day-19-crud-operations/
├── schema.sql                      # Database, tables, constraints, foreign keys, indexes
├── seed.sql                        # 5 departments and 32 diverse employee records
├── queries.sql                     # Solutions to 12 filtering, sorting, & subquery questions
├── transaction-safe-updates.sql    # Atomic transactions with rollback/commit patterns
└── README.md                       # Documentation, question catalog, & query breakdown
```

---

## Database Architecture

### `departments` Table
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `department_id` | `INT` | PRIMARY KEY, AUTO_INCREMENT | Unique department identifier |
| `department_name`| `VARCHAR(50)` | NOT NULL, UNIQUE | Human-readable name |
| `location` | `VARCHAR(100)`| NOT NULL | Campus / Floor address |
| `annual_budget` | `DECIMAL(14,2)`| NOT NULL, CHECK $\ge 0$ | Operating annual budget |

### `employees` Table
| Column | Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `employee_id` | `INT` | PRIMARY KEY, AUTO_INCREMENT | Unique employee record ID |
| `first_name` | `VARCHAR(50)` | NOT NULL | Given name |
| `last_name` | `VARCHAR(50)` | NOT NULL | Surname |
| `email` | `VARCHAR(120)`| NOT NULL, UNIQUE | Corporate email address |
| `hire_date` | `DATE` | NOT NULL | Date of joining |
| `job_title` | `VARCHAR(60)` | NOT NULL | Position |
| `salary` | `DECIMAL(10,2)`| NOT NULL, CHECK $> 0$ | Base annual salary |
| `department_id`| `INT` | FOREIGN KEY $\to$ `departments` | Assigned department |
| `manager_id` | `INT` | FOREIGN KEY $\to$ `employees` | Direct supervisor (Self-reference) |
| `status` | `ENUM` | 'ACTIVE', 'ON_LEAVE', 'TERMINATED' | Employment status |

---

## 12 SQL Filtering & Sorting Catalog

| # | Question / Requirement | SQL Feature / Clauses Used |
| :---: | :--- | :--- |
| **Q1** | Find employees with salary between $60,000 and $90,000 | `BETWEEN ... AND ...`, `ORDER BY salary DESC` |
| **Q2** | Find all employees whose first name starts with 'A' | `LIKE 'A%'`, `ORDER BY first_name ASC` |
| **Q3** | Find employees in Engineering or Finance departments | `JOIN`, `IN ('Engineering', 'Finance')` |
| **Q4** | Identify executive leadership with no direct manager | `WHERE manager_id IS NULL` |
| **Q5** | Top 5 highest paid employees across the enterprise | `JOIN`, `ORDER BY salary DESC LIMIT 5` |
| **Q6** | Employees hired on/after 2023-01-01 earning $> \$70,000$ | Date comparison (`>= '2023-01-01'`), `AND salary > 70000` |
| **Q7** | Employees with 'Engineer' or 'Developer' in job title | `LIKE '%Engineer%' OR LIKE '%Developer%'` |
| **Q8** | Department headcount, average, min, and max salary | `GROUP BY`, `COUNT()`, `AVG()`, `MIN()`, `MAX()` |
| **Q9** | Departments where average salary exceeds $80,000 | `GROUP BY`, `HAVING AVG(salary) > 80000` |
| **Q10**| Active employees with corporate email `@hcl.com` | `LIKE '%@hcl.com'`, `status = 'ACTIVE'` |
| **Q11**| Multi-column sort: Department ascending, Salary descending | `ORDER BY department_name ASC, salary DESC` |
| **Q12**| Employees earning above their department's average salary | **Correlated Subquery**: `salary > (SELECT AVG(...) WHERE dept_id)` |

---

## Safe Transaction Updates Best Practices

In production databases, unconstrained `UPDATE` statements can accidentally overwrite data across an entire table. Safe update practices enforce:

1. **Transaction Isolation (`START TRANSACTION`)**:
   - Isolates pending writes in memory so other concurrent connections continue reading consistent snapshots.
2. **Strict `WHERE` Predicate**:
   - Always filter updates on indexed attributes or primary keys to prevent unintentional table-wide mutations.
3. **Pre- and Post-Validation**:
   - Query staged rows within the uncommitted transaction before finalizing.
4. **`ROLLBACK` on Anomaly**:
   - If row count mismatch or business constraint validation fails, execute `ROLLBACK` immediately to revert state.
5. **`COMMIT` on Success**:
   - Only commit after verifying consistency.

---

## How to Execute Scripts

In MySQL Command Line or Workbench:
```bash
# 1. Create Schema
mysql -u root -p < schema.sql

# 2. Load Seed Data
mysql -u root -p < seed.sql

# 3. Run Query Catalog
mysql -u root -p < queries.sql

# 4. Run Transactional Updates Demo
mysql -u root -p < transaction-safe-updates.sql
```
