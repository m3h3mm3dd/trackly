package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long>,
        JpaSpecificationExecutor<AuditLog> {}
