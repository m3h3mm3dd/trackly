package com.m3h3mm3dd.trackly.controller;

import com.m3h3mm3dd.trackly.model.User;
import com.m3h3mm3dd.trackly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public User create(@RequestBody User u) { return service.create(u); }

    @GetMapping
    public Page<User> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "20") int size) {
        return service.list(page, size);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) { return service.get(id); }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User u) { return service.update(id, u); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
