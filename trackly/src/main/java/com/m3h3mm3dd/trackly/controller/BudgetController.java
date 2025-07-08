package com.m3h3mm3dd.trackly.controller;

import com.m3h3mm3dd.trackly.model.Budget;
import com.m3h3mm3dd.trackly.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @PostMapping
    public Budget create(@RequestBody Budget b) { return service.create(b); }

    @GetMapping("/{id}")
    public Budget get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public Budget update(@PathVariable Long id, @RequestBody Budget b) { return service.update(id, b); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public Page<Budget> list(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "20") int size,
                             @RequestParam(required = false) Long userId,
                             @RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false, defaultValue = "false") boolean active,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate onDate) {
        return service.list(page, size, userId, categoryId, active, onDate);
    }
}
