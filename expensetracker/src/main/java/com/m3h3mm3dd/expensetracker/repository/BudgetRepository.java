package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BudgetRepository
        extends JpaRepository<Budget, Long>,
        JpaSpecificationExecutor<Budget> {}
