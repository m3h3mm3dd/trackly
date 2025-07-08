package com.m3h3mm3dd.trackly.repository;

import com.m3h3mm3dd.trackly.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BudgetRepository
        extends JpaRepository<Budget, Long>,
        JpaSpecificationExecutor<Budget> {}
