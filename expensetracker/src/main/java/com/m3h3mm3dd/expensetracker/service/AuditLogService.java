package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.AuditLog;
import com.m3h3mm3dd.expensetracker.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.management.Query.and;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repo;

    public AuditLog get(Long id) { return repo.findById(id).orElseThrow(); }

    public Page<AuditLog> list(int page, int size,
                               Long actorId, String action,
                               LocalDate from, LocalDate to) {

        Specification<AuditLog> s = Specification.<AuditLog>where(null)
                .and(actorId == null ? null : (r,q,cb)-> cb.equal(r.get("actor").get("id"), actorId))
                .and(action  == null ? null : (r,q,cb)-> cb.like(cb.lower(r.get("action")), "%" + action.toLowerCase() + "%"))
                .and(from    == null ? null : (r,q,cb)-> cb.greaterThanOrEqualTo(r.get("createdAt"), from.atStartOfDay()))
                .and(to      == null ? null : (r,q,cb)-> cb.lessThan(r.get("createdAt"), to.plusDays(1).atStartOfDay()));

        return repo.findAll(s, PageRequest.of(page, size));
    }
}
