package com.m3h3mm3dd.trackly.service;

import com.m3h3mm3dd.trackly.exception.DuplicateResourceException;
import com.m3h3mm3dd.trackly.exception.ResourceNotFoundException;
import com.m3h3mm3dd.trackly.model.User;
import com.m3h3mm3dd.trackly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository repo;

    public User create(User u) {
        try {
            return repo.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Username or email already exists");
        }
    }

    @Transactional(readOnly = true)
    public User get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<User> list(int page, int size) {
        return repo.findAll(PageRequest.of(page, size));
    }

    public User update(Long id, User u) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        u.setId(id);
        try {
            return repo.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Username or email already exists");
        }
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        repo.deleteById(id);
    }
}