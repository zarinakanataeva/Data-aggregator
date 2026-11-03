package com.fintracker.service;

import com.fintracker.controller.dto.AggregateResultDto;
import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import com.fintracker.data.model.RegularTransaction;
import com.fintracker.data.model.TaxableTransaction;
import com.fintracker.data.model.Transaction;
import com.fintracker.data.repository.TransactionRepository;
import com.fintracker.service.TransactionService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionServiceTest {
    @Test
    void calculatesSumUsingDecimalAndNetTaxableAmount() {
        List<Transaction> transactions = List.of(
                new RegularTransaction(1, 1, LocalDateTime.parse("2025-01-01T10:00:00"),
                        "Food", new BigDecimal("-20.50"), "Regular"),
                new TaxableTransaction(1, 2, LocalDateTime.parse("2025-01-02T10:00:00"),
                        "Salary", new BigDecimal("100.00"), "Taxable", new BigDecimal("0.20"))
        );
        TransactionRepository repository = () -> transactions;
        TransactionService service = new TransactionService(repository);

        var analytic = service.calculateAnalytics(new TransactionFilterDto(),
                GroupOption.GROUP_BY_CATEGORY, AggregateOption.SUM_COUNT);

        AggregateResultDto food = analytic.getData().get("Food");
        AggregateResultDto salary = analytic.getData().get("Salary");
        assertEquals(new BigDecimal("-20.50"), food.getSum());
        assertEquals(new BigDecimal("80.00"), salary.getSum());
    }
}
