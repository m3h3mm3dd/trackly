package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.Income;
import com.m3h3mm3dd.expensetracker.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository repo;

    public Income create(Income i) { return repo.save(i); }

    public Income get(Long id)     { return repo.findById(id).orElseThrow(); }

    public Income update(Long id, Income i) { i.setId(id); return repo.save(i); }

    public void delete(Long id) { repo.deleteById(id); }

    public Page<Income> list(
            int page, int size,
            Long userId, Long categoryId,
            LocalDate from, LocalDate to,
            BigDecimal min, BigDecimal max) {

        Specification<Income> s = Specification.<Income>where(null)
                .and(userId    == null ? null : (r,q,cb)-> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId== null ? null : (r,q,cb)-> cb.equal(r.get("category").get("id"), categoryId))
                .and(from      == null ? null : (r,q,cb)-> cb.greaterThanOrEqualTo(r.get("date"), from))
                .and(to        == null ? null : (r,q,cb)-> cb.lessThanOrEqualTo(r.get("date"), to))
                .and(min       == null ? null : (r,q,cb)-> cb.ge(r.get("amount"), min))
                .and(max       == null ? null : (r,q,cb)-> cb.le(r.get("amount"), max));

        return repo.findAll(s, PageRequest.of(page, size));
    }
}
