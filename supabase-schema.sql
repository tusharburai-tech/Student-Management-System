-- ==========================================================
-- Student Management System (SMS) - Supabase PostgreSQL Schema
-- ==========================================================
-- Note: Spring Boot automatically creates and updates these tables on startup 
-- when connected with 'spring.jpa.hibernate.ddl-auto=update'.
-- You can also run this script directly in the Supabase SQL Editor if desired.

-- 1. Admin Users Table
CREATE TABLE IF NOT EXISTS admin_users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    designation VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Courses Table
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(50) NOT NULL UNIQUE,
    course_name VARCHAR(255) NOT NULL,
    department VARCHAR(100) NOT NULL,
    description TEXT,
    duration VARCHAR(50),
    semester VARCHAR(50),
    capacity INTEGER DEFAULT 50,
    current_enrollment INTEGER DEFAULT 0,
    instructor VARCHAR(255),
    status VARCHAR(50) DEFAULT 'Active',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 3. Students Table
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20),
    profile_image VARCHAR(500),
    department VARCHAR(100) NOT NULL,
    course_name VARCHAR(255),
    semester VARCHAR(50) DEFAULT 'Semester 1',
    enrollment_date DATE DEFAULT CURRENT_DATE,
    academic_year VARCHAR(50) DEFAULT '2024-2028',
    status VARCHAR(50) DEFAULT 'ACTIVE',
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pin_code VARCHAR(20),
    gpa DOUBLE PRECISION DEFAULT 3.65,
    attendance_rate DOUBLE PRECISION DEFAULT 92.5,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 4. Attendance Table
CREATE TABLE IF NOT EXISTS attendance (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id BIGINT REFERENCES courses(id) ON DELETE SET NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Activity Logs Table
CREATE TABLE IF NOT EXISTS activity_logs (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(500),
    activity_type VARCHAR(50),
    icon VARCHAR(100),
    badge_class VARCHAR(100),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for optimal query performance
CREATE INDEX IF NOT EXISTS idx_students_dept ON students(department);
CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(attendance_date);
CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);
