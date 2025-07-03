package com.m3h3mm3dd.expensetracker.controller;

import com.m3h3mm3dd.expensetracker.model.AuditLog;
import com.m3h3mm3dd.expensetracker.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService service;

    @GetMapping("/{id}")
    public AuditLog get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public Page<AuditLog> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) Long actorId,
                               @RequestParam(required = false) String action,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.list(page, size, actorId, action, from, to);
    }
}
