package com.m3h3mm3dd.trackly.repository;

import com.m3h3mm3dd.trackly.model.MoneyEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MoneyEntryRepository
        extends JpaRepository<MoneyEntry, Long>,
        JpaSpecificationExecutor<MoneyEntry> {

    /** Retrieve all money entries for a user ordered by date descending. */
    @Query("select m from MoneyEntry m where m.user.id = :userId order by m.date desc")
    List<MoneyEntry> findByUserIdOrderByDateDesc(@Param("userId") Long userId);

    /** Retrieve all money entries for a category ordered by date descending. */
    @Query("select m from MoneyEntry m where m.category.id = :categoryId order by m.date desc")
    List<MoneyEntry> findByCategoryIdOrderByDateDesc(@Param("categoryId") Long categoryId);

    /** Sum the amounts for a given user. */
    @Query("select coalesce(sum(m.amount),0) from MoneyEntry m where m.user.id = :userId")
    BigDecimal sumAmountByUserId(@Param("userId") Long userId);

    /** Sum the amounts for a user within an optional date range. */
    @Query("""
            select coalesce(sum(m.amount),0) from MoneyEntry m
            where m.user.id = :userId
              and (:from is null or m.date >= :from)
              and (:to   is null or m.date <= :to)
            """)
    BigDecimal sumAmountByUserIdAndDateRange(@Param("userId") Long userId,
                                             @Param("from") LocalDate from,
                                             @Param("to") LocalDate to);

    /** Sum the amounts for a given category. */
    @Query("select coalesce(sum(m.amount),0) from MoneyEntry m where m.category.id = :categoryId")
    BigDecimal sumAmountByCategoryId(@Param("categoryId") Long categoryId);

    /** Count entries for a user. */
    @Query("select count(m) from MoneyEntry m where m.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    /** Check existence of an entry for a user and category. */
    @Query("select count(m) > 0 from MoneyEntry m where m.user.id = :userId and m.category.id = :categoryId")
    boolean existsByUserIdAndCategoryId(@Param("userId") Long userId,
                                        @Param("categoryId") Long categoryId);
}
