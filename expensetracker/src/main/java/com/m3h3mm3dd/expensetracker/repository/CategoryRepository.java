package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Page<Category> findByUser_Id(Long userId, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findByUser_IdAndNameContainingIgnoreCase(Long userId,
                                                            String name,
                                                            Pageable pageable);
}