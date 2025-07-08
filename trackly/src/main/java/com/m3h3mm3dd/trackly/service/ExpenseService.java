package com.m3h3mm3dd.trackly.service;

import com.m3h3mm3dd.trackly.exception.ResourceNotFoundException;
import com.m3h3mm3dd.trackly.exception.ValidationException;
import com.m3h3mm3dd.trackly.model.Expense;
import com.m3h3mm3dd.trackly.repository.ExpenseRepository;
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
public class ExpenseService {

    private final ExpenseRepository repo;

    public Expense create(Expense e) {
        validateExpense(e);
        return repo.save(e);
    }

    @Transactional(readOnly = true)
    public Expense get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", "id", id));
    }

    public Expense update(Long id, Expense e) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Expense", "id", id);
        }
        validateExpense(e);
        e.setId(id);
        return repo.save(e);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Expense", "id", id);
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Expense> list(
            int page, int size,
            Long userId, Long categoryId,
            LocalDate from, LocalDate to,
            BigDecimal min, BigDecimal max) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new ValidationException("Minimum amount cannot be greater than maximum amount");
        }

        Specification<Expense> s = Specification.<Expense>where(null)
                .and(userId == null ? null : (r, q, cb) -> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId == null ? null : (r, q, cb) -> cb.equal(r.get("category").get("id"), categoryId))
                .and(from == null ? null : (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("date"), from))
                .and(to == null ? null : (r, q, cb) -> cb.lessThanOrEqualTo(r.get("date"), to))
                .and(min == null ? null : (r, q, cb) -> cb.ge(r.get("amount"), min))
                .and(max == null ? null : (r, q, cb) -> cb.le(r.get("amount"), max));

        return repo.findAll(s, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));
    }

    private void validateExpense(Expense expense) {
        if (expense.getAmount() != null && expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }

        if (expense.getDate() != null && expense.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Expense date cannot be in the future");
        }
    }
}