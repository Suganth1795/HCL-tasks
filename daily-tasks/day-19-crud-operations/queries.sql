-- ============================================================================
-- DAY 19: 12 FILTERING & SORTING SQL EXERCISES
-- ============================================================================

USE enterprise_directory;

-- ----------------------------------------------------------------------------
-- Query 1: Find employees with salary between $60,000 and $90,000 ordered by salary DESC
-- Operator: BETWEEN ... AND ...
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    CONCAT(first_name, ' ', last_name) AS full_name,
    job_title,
    salary
FROM employees
WHERE salary BETWEEN 60000.00 AND 90000.00
ORDER BY salary DESC;

-- ----------------------------------------------------------------------------
-- Query 2: Find all employees whose first name starts with 'A'
-- Operator: LIKE 'A%'
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    first_name,
    last_name,
    email,
    job_title,
    salary
FROM employees
WHERE first_name LIKE 'A%'
ORDER BY first_name ASC;

-- ----------------------------------------------------------------------------
-- Query 3: Find employees in specific departments (Engineering, Finance)
-- Operator: IN (subquery or set)
-- ----------------------------------------------------------------------------
SELECT 
    e.employee_id,
    CONCAT(e.first_name, ' ', e.last_name) AS full_name,
    d.department_name,
    e.job_title,
    e.salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
WHERE d.department_name IN ('Engineering', 'Finance')
ORDER BY d.department_name ASC, e.salary DESC;

-- ----------------------------------------------------------------------------
-- Query 4: Identify executive leadership (employees who do not report to any manager)
-- Operator: IS NULL
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    CONCAT(first_name, ' ', last_name) AS executive_name,
    job_title,
    salary,
    hire_date
FROM employees
WHERE manager_id IS NULL;

-- ----------------------------------------------------------------------------
-- Query 5: Retrieve Top 5 highest paid employees across the enterprise with department
-- Clauses: INNER JOIN, ORDER BY ... DESC, LIMIT
-- ----------------------------------------------------------------------------
SELECT 
    e.employee_id,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    d.department_name,
    e.job_title,
    e.salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
ORDER BY e.salary DESC
LIMIT 5;

-- ----------------------------------------------------------------------------
-- Query 6: Find employees hired on or after 2023-01-01 earning more than $70,000
-- Operators: Date comparison >=, AND, >
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    CONCAT(first_name, ' ', last_name) AS full_name,
    hire_date,
    job_title,
    salary
FROM employees
WHERE hire_date >= '2023-01-01' AND salary > 70000.00
ORDER BY hire_date ASC;

-- ----------------------------------------------------------------------------
-- Query 7: Search employees whose title contains 'Engineer' or 'Developer'
-- Operators: LIKE '%Engineer%' OR LIKE '%Developer%'
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    CONCAT(first_name, ' ', last_name) AS engineer_name,
    job_title,
    salary
FROM employees
WHERE job_title LIKE '%Engineer%' OR job_title LIKE '%Developer%'
ORDER BY salary DESC;

-- ----------------------------------------------------------------------------
-- Query 8: Department-wise headcount, average salary, minimum, and maximum salary
-- Aggregates: COUNT, AVG, MIN, MAX, GROUP BY
-- ----------------------------------------------------------------------------
SELECT 
    d.department_name,
    COUNT(e.employee_id) AS total_employees,
    ROUND(AVG(e.salary), 2) AS avg_salary,
    MIN(e.salary) AS min_salary,
    MAX(e.salary) AS max_salary
FROM departments d
LEFT JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_id, d.department_name
ORDER BY avg_salary DESC;

-- ----------------------------------------------------------------------------
-- Query 9: Filter departments where average salary exceeds $80,000
-- Clauses: GROUP BY ... HAVING
-- ----------------------------------------------------------------------------
SELECT 
    d.department_name,
    COUNT(e.employee_id) AS total_headcount,
    ROUND(AVG(e.salary), 2) AS average_salary
FROM departments d
JOIN employees e ON d.department_id = e.department_id
GROUP BY d.department_id, d.department_name
HAVING AVG(e.salary) > 80000.00
ORDER BY average_salary DESC;

-- ----------------------------------------------------------------------------
-- Query 10: Find employees with company corporate email format ending in '@hcl.com'
-- Operator: LIKE '%@hcl.com'
-- ----------------------------------------------------------------------------
SELECT 
    employee_id,
    CONCAT(first_name, ' ', last_name) AS full_name,
    email,
    status
FROM employees
WHERE email LIKE '%@hcl.com' AND status = 'ACTIVE'
ORDER BY last_name ASC;

-- ----------------------------------------------------------------------------
-- Query 11: Multi-column hierarchical sort (Department ASC, Salary DESC)
-- Clauses: ORDER BY department_name ASC, salary DESC
-- ----------------------------------------------------------------------------
SELECT 
    d.department_name,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    e.job_title,
    e.salary
FROM employees e
JOIN departments d ON e.department_id = d.department_id
ORDER BY d.department_name ASC, e.salary DESC;

-- ----------------------------------------------------------------------------
-- Query 12: Find employees earning ABOVE the average salary of their own department
-- Concept: Correlated Subquery
-- ----------------------------------------------------------------------------
SELECT 
    e1.employee_id,
    CONCAT(e1.first_name, ' ', e1.last_name) AS employee_name,
    d.department_name,
    e1.salary,
    ROUND((SELECT AVG(e2.salary) 
           FROM employees e2 
           WHERE e2.department_id = e1.department_id), 2) AS dept_avg_salary
FROM employees e1
JOIN departments d ON e1.department_id = d.department_id
WHERE e1.salary > (
    SELECT AVG(e2.salary)
    FROM employees e2
    WHERE e2.department_id = e1.department_id
)
ORDER BY d.department_name ASC, e1.salary DESC;
