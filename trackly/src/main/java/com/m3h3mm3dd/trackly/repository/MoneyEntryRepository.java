package com.m3h3mm3dd.trackly.repository;

import com.m3h3mm3dd.trackly.model.MoneyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MoneyEntryRepository
        extends JpaRepository<MoneyEntry, Long>,
        JpaSpecificationExecutor<MoneyEntry> {}
