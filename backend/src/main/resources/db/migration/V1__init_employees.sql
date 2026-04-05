CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(150) NOT NULL,
    salary DECIMAL(12, 2) NOT NULL,
    hire_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    phone_number VARCHAR(20),
    profile_image_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_employees_email ON employees(email);
CREATE INDEX idx_employees_department ON employees(department);
CREATE INDEX idx_employees_status ON employees(status);
CREATE INDEX idx_employees_last_name ON employees(last_name);

-- Seed data
INSERT INTO employees (first_name, last_name, email, department, position, salary, hire_date, status)
VALUES
  ('Alice', 'Johnson', 'alice.johnson@company.com', 'Engineering', 'Senior Developer', 95000, '2021-03-15', 'ACTIVE'),
  ('Bob', 'Smith', 'bob.smith@company.com', 'Engineering', 'Backend Engineer', 85000, '2022-06-01', 'ACTIVE'),
  ('Carol', 'Williams', 'carol.williams@company.com', 'HR', 'HR Manager', 80000, '2020-01-10', 'ACTIVE'),
  ('David', 'Brown', 'david.brown@company.com', 'Sales', 'Account Executive', 72000, '2023-02-20', 'ACTIVE'),
  ('Eve', 'Davis', 'eve.davis@company.com', 'Engineering', 'Frontend Developer', 88000, '2022-09-15', 'ON_LEAVE'),
  ('Frank', 'Miller', 'frank.miller@company.com', 'Finance', 'Financial Analyst', 75000, '2021-11-05', 'ACTIVE'),
  ('Grace', 'Wilson', 'grace.wilson@company.com', 'Marketing', 'Marketing Lead', 78000, '2020-07-22', 'ACTIVE'),
  ('Henry', 'Moore', 'henry.moore@company.com', 'Engineering', 'DevOps Engineer', 92000, '2021-05-18', 'ACTIVE');