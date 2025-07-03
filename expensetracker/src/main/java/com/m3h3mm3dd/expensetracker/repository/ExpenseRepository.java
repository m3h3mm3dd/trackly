package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {}
