package com.m3h3mm3dd.trackly.service;

import com.m3h3mm3dd.trackly.exception.DuplicateResourceException;
import com.m3h3mm3dd.trackly.exception.ResourceNotFoundException;
import com.m3h3mm3dd.trackly.model.Category;
import com.m3h3mm3dd.trackly.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository repo;

    public Category create(Category c) {
        try {
            return repo.save(c);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Category name already exists");
        }
    }

    @Transactional(readOnly = true)
    public Category get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<Category> list(int page, int size, Long userId, String name) {
        var pr = PageRequest.of(page, size);

        if (userId == null && name == null) return repo.findAll(pr);
        if (userId != null && name == null) return repo.findByUser_Id(userId, pr);
        if (userId == null) return repo.findByNameContainingIgnoreCase(name, pr);

        return repo.findByUser_IdAndNameContainingIgnoreCase(userId, name, pr);
    }

    public Category update(Long id, Category c) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        c.setId(id);
        try {
            return repo.save(c);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Category name already exists");
        }
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Category", "id", id);
        }
        repo.deleteById(id);
    }
}