package com.m3h3mm3dd.trackly.controller;

import com.m3h3mm3dd.trackly.model.MoneyEntry;
import com.m3h3mm3dd.trackly.service.MoneyEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class MoneyEntryController {

    private final MoneyEntryService service;

    @GetMapping("/{id}")
    public MoneyEntry get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public Page<MoneyEntry> list(@RequestParam(defaultValue = "0") int page,
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
