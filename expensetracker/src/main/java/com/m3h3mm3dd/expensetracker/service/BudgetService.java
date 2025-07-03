package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.Budget;
import com.m3h3mm3dd.expensetracker.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repo;

    public Budget create(Budget b) { return repo.save(b); }
    public Budget get(Long id)     { return repo.findById(id).orElseThrow(); }
    public Budget update(Long id, Budget b) { b.setId(id); return repo.save(b); }
    public void delete(Long id)    { repo.deleteById(id); }

    public Page<Budget> list(int page, int size,
                             Long userId, Long categoryId,
                             boolean active, LocalDate onDate) {

        LocalDate today = onDate != null ? onDate : LocalDate.now();

        Specification<Budget> s = Specification.<Budget>where(null)
                .and(userId    == null ? null : (r,q,cb)-> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId== null ? null : (r,q,cb)-> cb.equal(r.get("category").get("id"), categoryId))
                .and(!active ? null :
                        (r,q,cb)-> cb.and(
                                cb.lessThanOrEqualTo(r.get("start"), today),
                                cb.greaterThanOrEqualTo(r.get("end"), today)
                        ));

        return repo.findAll(s, PageRequest.of(page, size));
    }
}
