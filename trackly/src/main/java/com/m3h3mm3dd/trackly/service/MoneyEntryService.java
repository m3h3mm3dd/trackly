// Path: expensetracker/src/main/java/com/m3h3mm3dd/expensetracker/service/MoneyEntryService.java
package com.m3h3mm3dd.trackly.service;

import com.m3h3mm3dd.trackly.exception.ResourceNotFoundException;
import com.m3h3mm3dd.trackly.exception.ValidationException;
import com.m3h3mm3dd.trackly.model.MoneyEntry;
import com.m3h3mm3dd.trackly.repository.MoneyEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoneyEntryService {

    private final MoneyEntryRepository repo;

    public MoneyEntry get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MoneyEntry", "id", id));
    }

    public Page<MoneyEntry> list(int page, int size,
                                 Long userId, Long categoryId,
                                 LocalDate from, LocalDate to,
                                 BigDecimal min, BigDecimal max) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Start date cannot be after end date");
        }

        if (min != null && max != null && min.compareTo(max) > 0) {
            throw new ValidationException("Minimum amount cannot be greater than maximum amount");
        }

        if (min != null && min.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Minimum amount cannot be negative");
        }

        Specification<MoneyEntry> spec = Specification.<MoneyEntry>where(null)
                .and(userId == null ? null : (r, q, cb) -> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId == null ? null : (r, q, cb) -> cb.equal(r.get("category").get("id"), categoryId))
                .and(from == null ? null : (r, q, cb) -> cb.greaterThanOrEqualTo(r.get("date"), from))
                .and(to == null ? null : (r, q, cb) -> cb.lessThanOrEqualTo(r.get("date"), to))
                .and(min == null ? null : (r, q, cb) -> cb.ge(r.get("amount"), min))
                .and(max == null ? null : (r, q, cb) -> cb.le(r.get("amount"), max));

        Sort sort = Sort.by(Sort.Direction.DESC, "date", "id");
        return repo.findAll(spec, PageRequest.of(page, size, sort));
    }

    @Transactional
    public MoneyEntry create(MoneyEntry moneyEntry) {
        validateMoneyEntry(moneyEntry);
        return repo.save(moneyEntry);
    }

    @Transactional
    public MoneyEntry update(Long id, MoneyEntry moneyEntry) {
        MoneyEntry existing = get(id);
        validateMoneyEntry(moneyEntry);

        existing.setAmount(moneyEntry.getAmount());
        existing.setDescription(moneyEntry.getDescription());
        existing.setDate(moneyEntry.getDate());
        existing.setCategory(moneyEntry.getCategory());
        existing.setUser(moneyEntry.getUser());

        return repo.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        MoneyEntry existing = get(id);
        repo.delete(existing);
    }

    public List<MoneyEntry> findByUserId(Long userId) {
        return repo.findByUserIdOrderByDateDesc(userId);
    }

    public List<MoneyEntry> findByCategoryId(Long categoryId) {
        return repo.findByCategoryIdOrderByDateDesc(categoryId);
    }

    public BigDecimal getTotalAmountByUserId(Long userId) {
        return repo.sumAmountByUserId(userId);
    }

    public BigDecimal getTotalAmountByUserIdAndDateRange(Long userId, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("Start date cannot be after end date");
        }
        return repo.sumAmountByUserIdAndDateRange(userId, from, to);
    }

    public BigDecimal getTotalAmountByCategoryId(Long categoryId) {
        return repo.sumAmountByCategoryId(categoryId);
    }

    public long countByUserId(Long userId) {
        return repo.countByUserId(userId);
    }

    public boolean existsByUserIdAndCategoryId(Long userId, Long categoryId) {
        return repo.existsByUserIdAndCategoryId(userId, categoryId);
    }

    private void validateMoneyEntry(MoneyEntry moneyEntry) {
        if (moneyEntry == null) {
            throw new ValidationException("MoneyEntry cannot be null");
        }

        if (moneyEntry.getAmount() == null) {
            throw new ValidationException("Amount is required");
        }

        if (moneyEntry.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("Amount cannot be zero");
        }

        if (moneyEntry.getDate() == null) {
            throw new ValidationException("Date is required");
        }

        if (moneyEntry.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Date cannot be in the future");
        }

        if (moneyEntry.getUser() == null) {
            throw new ValidationException("User is required");
        }

        if (moneyEntry.getCategory() == null) {
            throw new ValidationException("Category is required");
        }

        if (moneyEntry.getDescription() != null && moneyEntry.getDescription().trim().length() > 500) {
            throw new ValidationException("Description cannot exceed 500 characters");
        }
    }
}