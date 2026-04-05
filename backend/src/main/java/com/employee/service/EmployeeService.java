package com.employee.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employee.dto.EmployeeDTO;
import com.employee.kafka.EmployeeEvent;
import com.employee.kafka.EmployeeEventProducer;
import com.employee.model.Employee;
import com.employee.model.Employee.EmployeeStatus;
import com.employee.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeEventProducer eventProducer;

    public EmployeeDTO.PageResponse getAllEmployees(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return toPageResponse(employees);
    }

    public EmployeeDTO.PageResponse searchEmployees(String search, String department,
                                                     String status, int page, int size) {
        EmployeeStatus employeeStatus = status != null ? EmployeeStatus.valueOf(status) : null;
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        Page<Employee> employees = employeeRepository.searchEmployees(
            search, department, employeeStatus, pageable
        );
        return toPageResponse(employees);
    }

    public EmployeeDTO.Response getEmployeeById(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public EmployeeDTO.Response createEmployee(EmployeeDTO.Request request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        Employee employee = Employee.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .department(request.getDepartment())
            .position(request.getPosition())
            .salary(request.getSalary())
            .hireDate(request.getHireDate())
            .status(request.getStatus() != null ? request.getStatus() : EmployeeStatus.ACTIVE)
            .phoneNumber(request.getPhoneNumber())
            .profileImageUrl(request.getProfileImageUrl())
            .build();

        Employee saved = employeeRepository.save(employee);
        log.info("Created employee: id={}, email={}", saved.getId(), saved.getEmail());

        eventProducer.publishEvent(
            EmployeeEvent.EventType.EMPLOYEE_CREATED,
            saved.getId(), saved.getEmail(), saved.getDepartment(), toResponse(saved)
        );

        return toResponse(saved);
    }

    @Transactional
    public EmployeeDTO.Response updateEmployee(Long id, EmployeeDTO.Request request) {
        Employee employee = findById(id);

        if (!employee.getEmail().equals(request.getEmail()) &&
            employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + request.getEmail());
        }

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setPosition(request.getPosition());
        employee.setSalary(request.getSalary());
        employee.setHireDate(request.getHireDate());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setProfileImageUrl(request.getProfileImageUrl());
        if (request.getStatus() != null) employee.setStatus(request.getStatus());

        Employee updated = employeeRepository.save(employee);
        log.info("Updated employee: id={}", updated.getId());

        eventProducer.publishEvent(
            EmployeeEvent.EventType.EMPLOYEE_UPDATED,
            updated.getId(), updated.getEmail(), updated.getDepartment(), toResponse(updated)
        );

        return toResponse(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = findById(id);
        employeeRepository.delete(employee);
        log.info("Deleted employee: id={}", id);

        eventProducer.publishEvent(
            EmployeeEvent.EventType.EMPLOYEE_DELETED,
            id, employee.getEmail(), employee.getDepartment(),
            Map.of("id", id, "email", employee.getEmail())
        );
    }

    @Transactional
    public EmployeeDTO.Response updateEmployeeStatus(Long id, EmployeeStatus status) {
        Employee employee = findById(id);
        EmployeeStatus oldStatus = employee.getStatus();
        employee.setStatus(status);
        Employee updated = employeeRepository.save(employee);

        eventProducer.publishEvent(
            EmployeeEvent.EventType.EMPLOYEE_STATUS_CHANGED,
            id, employee.getEmail(), employee.getDepartment(),
            Map.of("oldStatus", oldStatus, "newStatus", status)
        );

        return toResponse(updated);
    }

    public List<String> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    public Map<String, Long> getEmployeeStats() {
        return Map.of(
            "total", employeeRepository.count(),
            "active", employeeRepository.countByStatus(EmployeeStatus.ACTIVE),
            "inactive", employeeRepository.countByStatus(EmployeeStatus.INACTIVE),
            "onLeave", employeeRepository.countByStatus(EmployeeStatus.ON_LEAVE),
            "terminated", employeeRepository.countByStatus(EmployeeStatus.TERMINATED)
        );
    }

    private Employee findById(Long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    private EmployeeDTO.Response toResponse(Employee e) {
        return EmployeeDTO.Response.builder()
            .id(e.getId())
            .firstName(e.getFirstName())
            .lastName(e.getLastName())
            .email(e.getEmail())
            .department(e.getDepartment())
            .position(e.getPosition())
            .salary(e.getSalary())
            .hireDate(e.getHireDate())
            .status(e.getStatus())
            .phoneNumber(e.getPhoneNumber())
            .profileImageUrl(e.getProfileImageUrl())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }

    private EmployeeDTO.PageResponse toPageResponse(Page<Employee> page) {
        return EmployeeDTO.PageResponse.builder()
            .content(page.getContent().stream().map(this::toResponse).toList())
            .page(page.getNumber())
            .size(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast())
            .build();
    }
}