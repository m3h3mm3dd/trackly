package com.m3h3mm3dd.trackly.controller;

import com.m3h3mm3dd.trackly.model.Expense;
import com.m3h3mm3dd.trackly.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService service;

    @PostMapping
    public Expense create(@RequestBody Expense e) { return service.create(e); }

    @GetMapping("/{id}")
    public Expense get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public Expense update(@PathVariable Long id, @RequestBody Expense e) { return service.update(id, e); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public Page<Expense> list(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              @RequestParam(required = false) Long userId,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                              @RequestParam(required = false) BigDecimal min,
                              @RequestParam(required = false) BigDecimal max) {
        return service.list(page, size, userId, categoryId, from, to, min, max);
    }
}
