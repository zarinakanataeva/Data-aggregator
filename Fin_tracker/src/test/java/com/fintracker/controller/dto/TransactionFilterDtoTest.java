package com.fintracker.controller.dto;

import com.fintracker.data.model.RegularTransaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFilterDtoTest {
    @Test
    void includesTransactionsThroughoutEndDate() {
        TransactionFilterDto filter = new TransactionFilterDto();
        filter.setStartDate(java.time.LocalDate.parse("2025-01-01"));
        filter.setEndDate(java.time.LocalDate.parse("2025-01-31"));
        var predicate = filter.buildPredicate();

        var lastMoment = new RegularTransaction(1, 1, LocalDateTime.parse("2025-01-31T23:59:59.999999999"),
                "Food", new BigDecimal("10.00"), "Regular");
        var nextDay = new RegularTransaction(1, 2, LocalDateTime.parse("2025-02-01T00:00:00"),
                "Food", new BigDecimal("10.00"), "Regular");

        assertTrue(predicate.test(lastMoment));
        assertFalse(predicate.test(nextDay));
    }

    @Test
    void rejectsInvertedDateRange() {
        TransactionFilterDto filter = new TransactionFilterDto();
        filter.setStartDate(java.time.LocalDate.parse("2025-02-01"));
        filter.setEndDate(java.time.LocalDate.parse("2025-01-01"));
        assertThrows(IllegalArgumentException.class, filter::buildPredicate);
    }
}


