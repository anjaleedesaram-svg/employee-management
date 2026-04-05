package com.employee.repository;

import com.employee.model.Employee;
import com.employee.model.Employee.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Employee> findByDepartment(String department, Pageable pageable);

    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);

    @Query("""
        SELECT e FROM Employee e
        WHERE (:search IS NULL OR
               LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(e.department) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:department IS NULL OR e.department = :department)
        AND (:status IS NULL OR e.status = :status)
    """)
    Page<Employee> searchEmployees(
        @Param("search") String search,
        @Param("department") String department,
        @Param("status") EmployeeStatus status,
        Pageable pageable
    );

    @Query("SELECT DISTINCT e.department FROM Employee e ORDER BY e.department")
    java.util.List<String> findAllDepartments();

    long countByStatus(EmployeeStatus status);
}