package com.example.Assignment3.repository;

import com.example.Assignment3.model.AuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditEntry, Long> {
}
