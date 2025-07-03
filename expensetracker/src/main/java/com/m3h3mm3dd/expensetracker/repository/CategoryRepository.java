package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByUserId(Long userId, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findByUserIdAndNameContainingIgnoreCase(Long userId,
                                                           String name,
                                                           Pageable pageable);
}
