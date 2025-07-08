package com.m3h3mm3dd.trackly.controller;

import com.m3h3mm3dd.trackly.model.Category;
import com.m3h3mm3dd.trackly.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public Category create(@RequestBody Category c) { return service.create(c); }

    @GetMapping("/{id}")
    public Category get(@PathVariable Long id) { return service.get(id); }

    @GetMapping
    public Page<Category> list(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(required = false) Long userId,
                               @RequestParam(required = false) String name) {
        return service.list(page, size, userId, name);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category c) { return service.update(id, c); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
