package com.m3h3mm3dd.trackly.repository;

import com.m3h3mm3dd.trackly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
