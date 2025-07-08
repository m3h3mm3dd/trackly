package com.m3h3mm3dd.trackly.repository;

import com.m3h3mm3dd.trackly.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IncomeRepository
        extends JpaRepository<Income, Long>,
        JpaSpecificationExecutor<Income> {}
