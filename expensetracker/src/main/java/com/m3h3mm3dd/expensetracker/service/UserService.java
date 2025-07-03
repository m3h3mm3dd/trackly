package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.User;
import com.m3h3mm3dd.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public User create(User u)                     { return repo.save(u); }
    public User get(Long id)                       { return repo.findById(id).orElseThrow(); }
    public Page<User> list(int page, int size)     { return repo.findAll(PageRequest.of(page, size)); }
    public User update(Long id, User u)            { u.setId(id); return repo.save(u); }
    public void delete(Long id)                    { repo.deleteById(id); }
}
