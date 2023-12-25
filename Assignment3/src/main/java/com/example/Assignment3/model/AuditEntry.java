package com.example.Assignment3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName;

    private String parameters;

    private LocalDateTime timestamp;

    public AuditEntry() {
    }

    public AuditEntry(Long id, String methodName, String parameters, LocalDateTime timestamp) {
        this.id = id;
        this.methodName = methodName;
        this.parameters = parameters;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditEntry{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                ", parameters='" + parameters + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
