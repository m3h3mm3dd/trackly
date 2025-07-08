package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.exception.ResourceNotFoundException;
import com.m3h3mm3dd.expensetracker.exception.ValidationException;
import com.m3h3mm3dd.expensetracker.model.Income;
import com.m3h3mm3dd.expensetracker.repository.IncomeRepository;
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
public class IncomeService {

    private final IncomeRepository repo;

    public Income create(Income i) {
        validateIncome(i);
        return repo.save(i);
    }

    @Transactional(readOnly = true)
    public Income get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income", "id", id));
    }

    public Income update(Long id, Income i) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Income", "id", id);
        }
        validateIncome(i);
        i.setId(id);
        return repo.save(i);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Income", "id", id);
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Income> list(
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

        Specification<Income> s = Specification.<Income>where(null)
                .and(userId == null ? null : (r, q, cb) -> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId == null ? null : (r, q, cb) -> cb.equal(r.get("category").get("id"), categoryId))
                .and(from == null ? null : (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("date"), from))
                .and(to == null ? null : (r, q, cb) -> cb.lessThanOrEqualTo(r.get("date"), to))
                .and(min == null ? null : (r, q, cb) -> cb.ge(r.get("amount"), min))
                .and(max == null ? null : (r, q, cb) -> cb.le(r.get("amount"), max));

        return repo.findAll(s, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date")));
    }

    private void validateIncome(Income income) {
        if (income.getAmount() != null && income.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be greater than zero");
        }

        if (income.getDate() != null && income.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Income date cannot be in the future");
        }
    }
}