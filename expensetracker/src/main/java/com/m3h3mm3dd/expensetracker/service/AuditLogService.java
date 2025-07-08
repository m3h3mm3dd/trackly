package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.exception.ResourceNotFoundException;
import com.m3h3mm3dd.expensetracker.exception.ValidationException;
import com.m3h3mm3dd.expensetracker.model.AuditLog;
import com.m3h3mm3dd.expensetracker.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditLogService {

    private final AuditLogRepository repo;

    public AuditLog get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", "id", id));
    }

    public Page<AuditLog> list(int page, int size,
                               Long actorId, String action,
                               LocalDate from, LocalDate to) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        Specification<AuditLog> s = Specification.<AuditLog>where(null)
                .and(actorId == null ? null : (r, q, cb) -> cb.equal(r.get("actor").get("id"), actorId))
                .and(action == null ? null : (r, q, cb) -> cb.like(cb.lower(r.get("action")), "%" + action.toLowerCase() + "%"))
                .and(from == null ? null : (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("createdAt"), from.atStartOfDay()))
                .and(to == null ? null : (r, q, cb) -> cb.lessThan(r.get("createdAt"), to.plusDays(1).atStartOfDay()));

        return repo.findAll(s, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @Transactional
    public AuditLog createAuditLog(AuditLog auditLog) {
        return repo.save(auditLog);
    }
}