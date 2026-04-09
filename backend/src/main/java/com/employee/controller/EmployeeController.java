package com.employee.controller;

import com.employee.dto.EmployeeDTO;
import com.employee.model.Employee.EmployeeStatus;
import com.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@CrossOrigin(
    origins = {
        "http://localhost:3000",
        "http://208.87.132.36:3000",
        "http://appemployee-management.info"
    },
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<EmployeeDTO.PageResponse> getAllEmployees(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "lastName") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return ResponseEntity.ok(employeeService.getAllEmployees(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    public ResponseEntity<EmployeeDTO.PageResponse> searchEmployees(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String department,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(employeeService.searchEmployees(search, department, status, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO.Response> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO.Response> createEmployee(
        @Valid @RequestBody EmployeeDTO.Request request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(employeeService.createEmployee(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO.Response> updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody EmployeeDTO.Request request
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeDTO.Response> updateStatus(
        @PathVariable Long id,
        @RequestParam EmployeeStatus status
    ) {
        return ResponseEntity.ok(employeeService.updateEmployeeStatus(id, status));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getDepartments() {
        return ResponseEntity.ok(employeeService.getAllDepartments());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(employeeService.getEmployeeStats());
    }
}
