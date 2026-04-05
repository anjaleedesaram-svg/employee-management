package com.employee.kafka;

import lombok.*;
import java.time.LocalDateTime;

public class EmployeeEvent {

    public enum EventType {
        EMPLOYEE_CREATED,
        EMPLOYEE_UPDATED,
        EMPLOYEE_DELETED,
        EMPLOYEE_STATUS_CHANGED
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private String eventId;
		private EventType eventType;
        private Long employeeId;
        private String employeeEmail;
        private String department;
        private Object data;
        private LocalDateTime timestamp;
        private String triggeredBy;
        
        
    }
}