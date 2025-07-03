package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.Category;
import com.m3h3mm3dd.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    public Category create(Category c) { return repo.save(c); }

    public Category get(Long id)       { return repo.findById(id).orElseThrow(); }

    public Page<Category> list(int page, int size, Long userId, String name) {
        var pr = PageRequest.of(page, size);

        if (userId == null && name == null) return repo.findAll(pr);
        if (userId != null && name == null) return repo.findByUser_Id(userId, pr);
        if (userId == null)                 return repo.findByNameContainingIgnoreCase(name, pr);

        return repo.findByUser_IdAndNameContainingIgnoreCase(userId, name, pr);
    }

    public Category update(Long id, Category c) { c.setId(id); return repo.save(c); }

    public void delete(Long id)                 { repo.deleteById(id); }
}