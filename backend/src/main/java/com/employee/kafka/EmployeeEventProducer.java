package com.employee.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeEventProducer {

    private final KafkaTemplate<String, EmployeeEvent.Payload> kafkaTemplate;

    @Value("${kafka.topic.employee-events:employee-events}")
    private String topic;

    public void publishEvent(EmployeeEvent.EventType eventType, Long employeeId,
                             String email, String department, Object data) {
        EmployeeEvent.Payload event = EmployeeEvent.Payload.builder()
            .eventId(UUID.randomUUID().toString())
            .eventType(eventType)
            .employeeId(employeeId)
            .employeeEmail(email)
            .department(department)
            .data(data)
            .timestamp(LocalDateTime.now())
            .triggeredBy("system")
            .build();

        CompletableFuture<SendResult<String, EmployeeEvent.Payload>> future =
            kafkaTemplate.send(topic, employeeId.toString(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Event published: type={}, employeeId={}, offset={}",
                    eventType, employeeId,
                    result.getRecordMetadata().offset());
            } else {
                log.error("Failed to publish event: type={}, employeeId={}, error={}",
                    eventType, employeeId, ex.getMessage());
            }
        });
    }
}