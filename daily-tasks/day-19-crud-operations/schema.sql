-- ============================================================================
-- DAY 19: CRUD OPERATIONS & ADVANCED SQL FILTERING
-- Database Schema: enterprise_directory
-- ============================================================================

DROP DATABASE IF EXISTS enterprise_directory;
CREATE DATABASE enterprise_directory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE enterprise_directory;

-- ----------------------------------------------------------------------------
-- Table: departments
-- ----------------------------------------------------------------------------
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL,
    annual_budget DECIMAL(14, 2) NOT NULL CHECK (annual_budget >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- Table: employees
-- ----------------------------------------------------------------------------
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(25) DEFAULT NULL,
    hire_date DATE NOT NULL,
    job_title VARCHAR(60) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL CHECK (salary > 0),
    department_id INT NOT NULL,
    manager_id INT DEFAULT NULL,
    status ENUM('ACTIVE', 'ON_LEAVE', 'TERMINATED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_emp_department FOREIGN KEY (department_id)
        REFERENCES departments(department_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_emp_manager FOREIGN KEY (manager_id)
        REFERENCES employees(employee_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Indexes for performance on frequently queried columns
CREATE INDEX idx_emp_salary ON employees(salary);
CREATE INDEX idx_emp_dept ON employees(department_id);
CREATE INDEX idx_emp_name ON employees(last_name, first_name);
