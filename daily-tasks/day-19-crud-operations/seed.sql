-- ============================================================================
-- DAY 19: SEED DATA (5 Departments, 32 Employees)
-- ============================================================================

USE enterprise_directory;

-- Departments Seed Data
INSERT INTO departments (department_id, department_name, location, annual_budget) VALUES
(1, 'Engineering', 'Building A, Floor 4', 2500000.00),
(2, 'Human Resources', 'Building B, Floor 2', 650000.00),
(3, 'Finance', 'Building A, Floor 5', 1200000.00),
(4, 'Marketing', 'Building C, Floor 1', 950000.00),
(5, 'Sales', 'Building C, Floor 3', 1800000.00);

-- Employees Seed Data (32 rows, diverse titles, salaries, managers, hire dates)
-- Leadership / Senior Managers (No manager_id or high level)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(1, 'Alexander', 'Wright', 'alexander.wright@hcl.com', '555-0101', '2019-03-15', 'VP of Engineering', 165000.00, 1, NULL, 'ACTIVE'),
(2, 'Amanda', 'Clark', 'amanda.clark@hcl.com', '555-0102', '2019-06-01', 'HR Director', 125000.00, 2, NULL, 'ACTIVE'),
(3, 'Arthur', 'Morgan', 'arthur.morgan@hcl.com', '555-0103', '2018-11-10', 'Chief Financial Officer', 175000.00, 3, NULL, 'ACTIVE'),
(4, 'Barbara', 'Gordon', 'barbara.gordon@hcl.com', '555-0104', '2020-01-20', 'Marketing Director', 130000.00, 4, NULL, 'ACTIVE'),
(5, 'Charles', 'Xavier', 'charles.xavier@hcl.com', '555-0105', '2019-08-14', 'VP of Global Sales', 155000.00, 5, NULL, 'ACTIVE');

-- Engineering Team (Reporting to Alexander Wright #1)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(6, 'Alan', 'Turing', 'alan.turing@hcl.com', '555-0106', '2020-04-01', 'Lead Software Engineer', 140000.00, 1, 1, 'ACTIVE'),
(7, 'Alice', 'Johnson', 'alice.johnson@hcl.com', '555-0107', '2021-02-15', 'Senior Backend Developer', 115000.00, 1, 6, 'ACTIVE'),
(8, 'Aaron', 'Rodgers', 'aaron.rodgers@hcl.com', '555-0108', '2022-07-10', 'Full Stack Developer', 92000.00, 1, 6, 'ACTIVE'),
(9, 'Benjamin', 'Franklin', 'benjamin.franklin@hcl.com', '555-0109', '2023-03-01', 'DevOps Engineer', 98000.00, 1, 6, 'ACTIVE'),
(10, 'Catherine', 'Zeta', 'catherine.zeta@hcl.com', '555-0110', '2023-09-18', 'Junior QA Engineer', 62000.00, 1, 7, 'ACTIVE'),
(11, 'Daniel', 'Craig', 'daniel.craig@hcl.com', '555-0111', '2024-01-15', 'Frontend Developer', 74000.00, 1, 7, 'ACTIVE'),
(12, 'Ananya', 'Sharma', 'ananya.sharma@hcl.com', '555-0112', '2024-05-20', 'Cloud Infrastructure Engineer', 88000.00, 1, 9, 'ACTIVE'),
(13, 'David', 'Beckham', 'david.beckham@hcl.com', '555-0113', '2022-11-05', 'Software Engineer II', 86000.00, 1, 7, 'ACTIVE');

-- Human Resources Team (Reporting to Amanda Clark #2)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(14, 'Angela', 'Davis', 'angela.davis@hcl.com', '555-0114', '2021-05-12', 'HR Business Partner', 82000.00, 2, 2, 'ACTIVE'),
(15, 'Brian', 'May', 'brian.may@hcl.com', '555-0115', '2022-03-22', 'Technical Recruiter', 68000.00, 2, 14, 'ACTIVE'),
(16, 'Chloe', 'Kim', 'chloe.kim@hcl.com', '555-0116', '2023-08-01', 'Talent Acquisition Specialist', 58000.00, 2, 14, 'ACTIVE'),
(17, 'Andrew', 'Garfield', 'andrew.garfield@hcl.com', '555-0117', '2024-02-10', 'HR Coordinator', 48000.00, 2, 14, 'ACTIVE');

-- Finance Team (Reporting to Arthur Morgan #3)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(18, 'Adam', 'Smith', 'adam.smith@hcl.com', '555-0118', '2020-08-15', 'Senior Financial Analyst', 105000.00, 3, 3, 'ACTIVE'),
(19, 'Diana', 'Prince', 'diana.prince@hcl.com', '555-0119', '2021-10-01', 'Accounting Manager', 95000.00, 3, 3, 'ACTIVE'),
(20, 'Edward', 'Norton', 'edward.norton@hcl.com', '555-0120', '2022-04-18', 'Payroll Specialist', 64000.00, 3, 19, 'ACTIVE'),
(21, 'Fiona', 'Gallagher', 'fiona.gallagher@hcl.com', '555-0121', '2023-06-30', 'Junior Financial Auditor', 56000.00, 3, 18, 'ACTIVE'),
(22, 'Anthony', 'Hopkins', 'anthony.hopkins@hcl.com', '555-0122', '2023-11-15', 'Tax Compliance Specialist', 78000.00, 3, 19, 'ACTIVE');

-- Marketing Team (Reporting to Barbara Gordon #4)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(23, 'George', 'Lucas', 'george.lucas@hcl.com', '555-0123', '2021-01-10', 'Content Marketing Lead', 88000.00, 4, 4, 'ACTIVE'),
(24, 'Hannah', 'Abbott', 'hannah.abbott@hcl.com', '555-0124', '2022-09-05', 'SEO & Analytics Specialist', 72000.00, 4, 23, 'ACTIVE'),
(25, 'Ian', 'McKellen', 'ian.mckellen@hcl.com', '555-0125', '2023-04-12', 'Brand Strategist', 84000.00, 4, 4, 'ACTIVE'),
(26, 'Amber', 'Heard', 'amber.heard@hcl.com', '555-0126', '2024-03-01', 'Social Media Coordinator', 52000.00, 4, 23, 'ACTIVE'),
(27, 'Julia', 'Roberts', 'julia.roberts@hcl.com', '555-0127', '2023-02-15', 'Digital Campaign Manager', 79000.00, 4, 4, 'ON_LEAVE');

-- Sales Team (Reporting to Charles Xavier #5)
INSERT INTO employees (employee_id, first_name, last_name, email, phone, hire_date, job_title, salary, department_id, manager_id, status) VALUES
(28, 'Kevin', 'Hart', 'kevin.hart@hcl.com', '555-0128', '2020-07-20', 'Enterprise Account Executive', 110000.00, 5, 5, 'ACTIVE'),
(29, 'Laura', 'Croft', 'laura.croft@hcl.com', '555-0129', '2021-11-15', 'Senior Sales Representative', 89000.00, 5, 28, 'ACTIVE'),
(30, 'Austin', 'Butler', 'austin.butler@hcl.com', '555-0130', '2023-01-10', 'Sales Development Rep', 61000.00, 5, 28, 'ACTIVE'),
(31, 'Michael', 'Scott', 'michael.scott@hcl.com', '555-0131', '2022-08-01', 'Regional Sales Manager', 96000.00, 5, 5, 'ACTIVE'),
(32, 'Natalie', 'Portman', 'natalie.portman@hcl.com', '555-0132', '2024-04-05', 'Inside Sales Specialist', 55000.00, 5, 31, 'ACTIVE');
