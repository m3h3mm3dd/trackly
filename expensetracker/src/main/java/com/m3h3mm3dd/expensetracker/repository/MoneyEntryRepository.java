package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.MoneyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MoneyEntryRepository
        extends JpaRepository<MoneyEntry, Long>,
        JpaSpecificationExecutor<MoneyEntry> {}
