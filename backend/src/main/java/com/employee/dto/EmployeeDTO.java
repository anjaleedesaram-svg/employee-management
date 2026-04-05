package com.employee.dto;

import com.employee.model.Employee.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Department is required")
        private String department;

        @NotBlank(message = "Position is required")
        private String position;

        @NotNull(message = "Salary is required")
        @DecimalMin(value = "0.00", message = "Salary must be positive")
        private BigDecimal salary;

        @NotNull(message = "Hire date is required")
        private LocalDate hireDate;

        private String phoneNumber;
        private String profileImageUrl;
        private EmployeeStatus status;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String department;
        private String position;
        private BigDecimal salary;
        private LocalDate hireDate;
        private EmployeeStatus status;
        private String phoneNumber;
        private String profileImageUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResponse {
        private java.util.List<Response> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}