package com.employee.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeEventConsumer {

    @KafkaListener(
        topics = "${kafka.topic.employee-events:employee-events}",
        groupId = "${spring.kafka.consumer.group-id:employee-group}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeEmployeeEvent(
        @Payload EmployeeEvent.Payload event,
        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("Received event: type={}, employeeId={}, topic={}, partition={}, offset={}",
            event.getEventType(), event.getEmployeeId(), topic, partition, offset);

        switch (event.getEventType()) {
            case EMPLOYEE_CREATED -> handleEmployeeCreated(event);
            case EMPLOYEE_UPDATED -> handleEmployeeUpdated(event);
            case EMPLOYEE_DELETED -> handleEmployeeDeleted(event);
            case EMPLOYEE_STATUS_CHANGED -> handleStatusChanged(event);
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }
    }

    private void handleEmployeeCreated(EmployeeEvent.Payload event) {
        log.info("Processing EMPLOYEE_CREATED: id={}, email={}",
            event.getEmployeeId(), event.getEmployeeEmail());
        // Add: send welcome email, provision accounts, etc.
    }

    private void handleEmployeeUpdated(EmployeeEvent.Payload event) {
        log.info("Processing EMPLOYEE_UPDATED: id={}", event.getEmployeeId());
        // Add: sync with LDAP, update other systems, etc.
    }

    private void handleEmployeeDeleted(EmployeeEvent.Payload event) {
        log.info("Processing EMPLOYEE_DELETED: id={}", event.getEmployeeId());
        // Add: revoke access, archive records, etc.
    }

    private void handleStatusChanged(EmployeeEvent.Payload event) {
        log.info("Processing EMPLOYEE_STATUS_CHANGED: id={}", event.getEmployeeId());
        // Add: notify HR, update payroll, etc.
    }
}