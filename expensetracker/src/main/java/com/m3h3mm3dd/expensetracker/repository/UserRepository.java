package com.m3h3mm3dd.expensetracker.repository;

import com.m3h3mm3dd.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
