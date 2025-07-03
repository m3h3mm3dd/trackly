package com.m3h3mm3dd.expensetracker.controller;

import com.m3h3mm3dd.expensetracker.model.Income;
import com.m3h3mm3dd.expensetracker.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService service;

    @PostMapping
    public Income create(@RequestBody Income i) { return service.create(i); }

    @GetMapping("/{id}")
    public Income get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public Income update(@PathVariable Long id, @RequestBody Income i) { return service.update(id, i); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public Page<Income> list(@RequestParam(defaultValue = "0") int page,
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
