package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.exception.ResourceNotFoundException;
import com.m3h3mm3dd.expensetracker.exception.ValidationException;
import com.m3h3mm3dd.expensetracker.model.Budget;
import com.m3h3mm3dd.expensetracker.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class BudgetService {

    private final BudgetRepository repo;

    public Budget create(Budget b) {
        validateBudget(b);
        return repo.save(b);
    }

    @Transactional(readOnly = true)
    public Budget get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget", "id", id));
    }

    public Budget update(Long id, Budget b) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Budget", "id", id);
        }
        validateBudget(b);
        b.setId(id);
        return repo.save(b);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Budget", "id", id);
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Budget> list(int page, int size,
                             Long userId, Long categoryId,
                             boolean active, LocalDate onDate) {

        LocalDate today = onDate != null ? onDate : LocalDate.now();

        Specification<Budget> s = Specification.<Budget>where(null)
                .and(userId == null ? null : (r, q, cb) -> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId == null ? null : (r, q, cb) -> cb.equal(r.get("category").get("id"), categoryId))
                .and(!active ? null :
                        (r, q, cb) -> cb.and(
                                cb.lessThanOrEqualTo(r.get("start"), today),
                                cb.greaterThanOrEqualTo(r.get("end"), today)
                        ));

        return repo.findAll(s, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    private void validateBudget(Budget budget) {
        if (budget.getLimitAmount() != null && budget.getLimitAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Budget limit amount must be greater than zero");
        }

        if (budget.getStart() != null && budget.getEnd() != null && budget.getStart().isAfter(budget.getEnd())) {
            throw new ValidationException("Budget start date cannot be after end date");
        }

        if (budget.getEnd() != null && budget.getEnd().isBefore(LocalDate.now())) {
            throw new ValidationException("Budget end date cannot be in the past");
        }
    }
}