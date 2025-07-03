package com.m3h3mm3dd.expensetracker.service;

import com.m3h3mm3dd.expensetracker.model.MoneyEntry;
import com.m3h3mm3dd.expensetracker.repository.MoneyEntryRepository;
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
public class MoneyEntryService {

    private final MoneyEntryRepository repo;

    public MoneyEntry get(Long id) { return repo.findById(id).orElseThrow(); }

    public Page<MoneyEntry> list(int page, int size,
                                 Long userId, Long categoryId,
                                 LocalDate from, LocalDate to,
                                 BigDecimal min, BigDecimal max) {

        Specification<MoneyEntry> s = Specification.<MoneyEntry>where(null)
                .and(userId    == null ? null : (r,q,cb)-> cb.equal(r.get("user").get("id"), userId))
                .and(categoryId== null ? null : (r,q,cb)-> cb.equal(r.get("category").get("id"), categoryId))
                .and(from      == null ? null : (r,q,cb)-> cb.greaterThanOrEqualTo(r.get("date"), from))
                .and(to        == null ? null : (r,q,cb)-> cb.lessThanOrEqualTo(r.get("date"), to))
                .and(min       == null ? null : (r,q,cb)-> cb.ge(r.get("amount"), min))
                .and(max       == null ? null : (r,q,cb)-> cb.le(r.get("amount"), max));

        return repo.findAll(s, PageRequest.of(page, size));
    }
}
