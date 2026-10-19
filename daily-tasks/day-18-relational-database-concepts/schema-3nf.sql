-- ============================================================================
-- DAY 18: RELATIONAL DATABASE CONCEPTS - 3NF NORMALIZED SCHEMA
-- Database: college_management_system
-- ============================================================================

DROP DATABASE IF EXISTS college_management;
CREATE DATABASE college_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE college_management;

-- ----------------------------------------------------------------------------
-- 1. DEPARTMENTS (Eliminates Transitive Dependency in Courses and Faculty)
-- ----------------------------------------------------------------------------
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(10) NOT NULL UNIQUE,
    department_name VARCHAR(100) NOT NULL,
    office_building VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. FACULTY (Eliminates Partial Dependency on Registration)
-- ----------------------------------------------------------------------------
CREATE TABLE faculty (
    faculty_id INT AUTO_INCREMENT PRIMARY KEY,
    department_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    office_room VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_faculty_department FOREIGN KEY (department_id) 
        REFERENCES departments(department_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. COURSES (Eliminates Partial Dependency on Registration)
-- ----------------------------------------------------------------------------
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(15) NOT NULL UNIQUE,
    department_id INT NOT NULL,
    course_title VARCHAR(150) NOT NULL,
    credits TINYINT UNSIGNED NOT NULL CHECK (credits BETWEEN 1 AND 6),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_courses_department FOREIGN KEY (department_id) 
        REFERENCES departments(department_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 4. STUDENTS (Eliminates Partial Dependency on Registration)
-- ----------------------------------------------------------------------------
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    student_number VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    residential_address VARCHAR(255) NOT NULL,
    enrolled_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 5. COURSE OFFERINGS / SECTIONS (Semester-specific assignments)
-- ----------------------------------------------------------------------------
CREATE TABLE course_offerings (
    offering_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    faculty_id INT NOT NULL,
    semester ENUM('Fall', 'Spring', 'Summer') NOT NULL,
    academic_year YEAR NOT NULL,
    capacity INT UNSIGNED DEFAULT 40,
    CONSTRAINT fk_offering_course FOREIGN KEY (course_id) 
        REFERENCES courses(course_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_offering_faculty FOREIGN KEY (faculty_id) 
        REFERENCES faculty(faculty_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_offering_slot UNIQUE (course_id, faculty_id, semester, academic_year)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 6. REGISTRATIONS (Bridge relation representing Student enrollment)
-- ----------------------------------------------------------------------------
CREATE TABLE registrations (
    registration_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    offering_id INT NOT NULL,
    registration_date DATE NOT NULL,
    grade VARCHAR(5) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reg_student FOREIGN KEY (student_id) 
        REFERENCES students(student_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_reg_offering FOREIGN KEY (offering_id) 
        REFERENCES course_offerings(offering_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uq_student_enrollment UNIQUE (student_id, offering_id)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 7. FEE PAYMENTS (Separates Financial Transaction details from Academic Records)
-- ----------------------------------------------------------------------------
CREATE TABLE fee_payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    registration_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL CHECK (amount >= 0),
    payment_status ENUM('PAID', 'PENDING', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'PENDING',
    payment_date DATE DEFAULT NULL,
    transaction_reference VARCHAR(64) UNIQUE DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fee_registration FOREIGN KEY (registration_id) 
        REFERENCES registrations(registration_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- VERIFICATION SEED DATA
-- ============================================================================

-- Departments
INSERT INTO departments (department_code, department_name, office_building) VALUES
('CS', 'Computer Science & Engineering', 'Turing Hall'),
('EC', 'Electronics & Communication', 'Tesla Wing'),
('ME', 'Mechanical Engineering', 'Newton Center');

-- Faculty
INSERT INTO faculty (department_id, full_name, email, office_room, phone) VALUES
(1, 'Dr. Robert Hill', 'rhill@univ.edu', 'TH-301', '9876500001'),
(1, 'Dr. Jane Smith', 'jsmith@univ.edu', 'TH-304', '9876500002'),
(2, 'Dr. David Ross', 'dross@univ.edu', 'TW-105', '9876500003');

-- Courses
INSERT INTO courses (course_code, department_id, course_title, credits) VALUES
('CS101', 1, 'Data Structures & Algorithms', 4),
('CS102', 1, 'Advanced Database Systems', 4),
('EC201', 2, 'Digital Signal Processing', 3);

-- Students
INSERT INTO students (student_number, first_name, last_name, email, phone, residential_address, enrolled_date) VALUES
('S1001', 'Alice', 'Walker', 'alice@univ.edu', '9876543210', '12 Park Ave, New York', '2024-08-01'),
('S1002', 'Brian', 'Adams', 'badams@univ.edu', '9123456780', '45 Elm St, New York', '2024-08-01'),
('S1003', 'Cathy', 'Zhang', 'czhang@univ.edu', '9988776655', '78 Oak Dr, New York', '2025-01-15');

-- Course Offerings (Fall 2026)
INSERT INTO course_offerings (course_id, faculty_id, semester, academic_year, capacity) VALUES
(1, 1, 'Fall', 2026, 60), -- CS101 by Dr. Hill
(2, 2, 'Fall', 2026, 45), -- CS102 by Dr. Smith
(3, 3, 'Fall', 2026, 50); -- EC201 by Dr. Ross

-- Registrations
INSERT INTO registrations (student_id, offering_id, registration_date, grade) VALUES
(1, 1, '2026-08-10', 'A'),   -- Alice in CS101
(1, 2, '2026-08-10', 'B+'),  -- Alice in CS102
(2, 1, '2026-08-12', 'A-'),  -- Brian in CS101
(3, 3, '2026-08-14', 'A');   -- Cathy in EC201

-- Fee Payments
INSERT INTO fee_payments (registration_id, amount, payment_status, payment_date, transaction_reference) VALUES
(1, 1200.00, 'PAID', '2026-08-15', 'TXN-2026-0001'),
(2, 1200.00, 'PAID', '2026-08-15', 'TXN-2026-0002'),
(3, 1200.00, 'PENDING', NULL, NULL),
(4, 900.00, 'PAID', '2026-08-18', 'TXN-2026-0003');

-- ============================================================================
-- VERIFICATION RECONSTRUCTION QUERY (Re-produces original flat view cleanly)
-- ============================================================================
SELECT 
    r.registration_id,
    s.student_number,
    CONCAT(s.first_name, ' ', s.last_name) AS student_name,
    c.course_code,
    c.course_title,
    d.department_name,
    f.full_name AS instructor_name,
    co.semester,
    co.academic_year,
    r.grade,
    fp.amount AS fee_amount,
    fp.payment_status,
    fp.payment_date
FROM registrations r
JOIN students s ON r.student_id = s.student_id
JOIN course_offerings co ON r.offering_id = co.offering_id
JOIN courses c ON co.course_id = c.course_id
JOIN departments d ON c.department_id = d.department_id
JOIN faculty f ON co.faculty_id = f.faculty_id
LEFT JOIN fee_payments fp ON r.registration_id = fp.registration_id
ORDER BY r.registration_id;
