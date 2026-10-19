-- ============================================================================
-- DAY 19: SAFE TRANSACTIONAL UPDATES DEMONSTRATION
-- Best Practices: Explicit Transactions, Rollback Verification, Row Locking
-- ============================================================================

USE enterprise_directory;

-- ============================================================================
-- SCENARIO 1: MERIT INCREASE SIMULATION WITH ROLLBACK
-- Requirement: Apply a 10% raise to all junior engineers (salary < $80,000)
-- but rollback after inspecting audit consequences.
-- ============================================================================

-- Step 1: Check baseline salaries before modification
SELECT employee_id, first_name, last_name, job_title, salary 
FROM employees 
WHERE department_id = 1 AND salary < 80000.00;

-- Step 2: Begin transactional unit of work
START TRANSACTION;

-- Step 3: Perform atomic update with strict WHERE predicate
UPDATE employees 
SET salary = ROUND(salary * 1.10, 2)
WHERE department_id = 1 AND salary < 80000.00;

-- Step 4: Verify staged in-flight changes within transaction
SELECT employee_id, first_name, last_name, job_title, salary 
FROM employees 
WHERE department_id = 1 AND salary < 90000.00;

-- Step 5: Rollback the entire transaction (Simulating failed business check or safety abort)
ROLLBACK;

-- Step 6: Verify salaries reverted safely to their original baseline
SELECT employee_id, first_name, last_name, job_title, salary 
FROM employees 
WHERE department_id = 1 AND salary < 80000.00;


-- ============================================================================
-- SCENARIO 2: CONTROLLED MERIT INCREASE WITH COMMIT
-- Requirement: Increase annual budget of Engineering by $200,000, 
-- and apply 5% cost-of-living adjustment to active employees in Engineering.
-- ============================================================================

START TRANSACTION;

-- 1. Update department budget
UPDATE departments 
SET annual_budget = annual_budget + 200000.00
WHERE department_id = 1;

-- 2. Update employee compensation
UPDATE employees 
SET salary = ROUND(salary * 1.05, 2)
WHERE department_id = 1 AND status = 'ACTIVE';

-- 3. Confirm all integrity constraints hold
SELECT d.department_name, d.annual_budget, COUNT(e.employee_id) AS active_engineers, SUM(e.salary) AS total_payroll
FROM departments d
JOIN employees e ON d.department_id = e.department_id
WHERE d.department_id = 1
GROUP BY d.department_id, d.department_name, d.annual_budget;

-- 4. Persist permanently to disk
COMMIT;

-- Verification post-commit
SELECT d.department_name, d.annual_budget, AVG(e.salary) AS new_avg_engineering_salary
FROM departments d
JOIN employees e ON d.department_id = e.department_id
WHERE d.department_id = 1
GROUP BY d.department_id, d.department_name, d.annual_budget;
