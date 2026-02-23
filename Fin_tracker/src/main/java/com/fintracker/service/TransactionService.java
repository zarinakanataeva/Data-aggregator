package com.fintracker.service;

import com.fintracker.controller.dto.AggregateResultDto;
import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import com.fintracker.data.model.*;
import com.fintracker.data.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс, обрабатывающий транзакции и их систематизацию.
 */

public class TransactionService implements TransactionServiceInterface {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Analytic calculateAnalytics(TransactionFilterDto filter, GroupOption group, AggregateOption aggregate) {
        List<Transaction> filtered = repository.readAll().stream()
                .filter(filter.buildPredicate())
                .collect(Collectors.toList());

        Map<String, AggregateResultDto> results = filtered.stream()
                .collect(Collectors.groupingBy(
                        t -> getGroupKey(t, group),
                        Collectors.collectingAndThen(Collectors.toList(),
                                list -> performAggregation(list, aggregate, filter.getStartDate(), filter.getEndDate()))
                ));
        return new Analytic(group, aggregate, filter, results);
    }


    private String getGroupKey(Transaction t, GroupOption option) {
        return switch (option) {
            case GROUP_BY_CATEGORY -> t.getCategory();
            case GROUP_BY_MONTH -> t.getDate().getMonth().name();
            case GROUP_BY_YEAR -> String.valueOf(t.getDate().getYear());
            case GROUP_BY_ACCOUNT_TYPE -> t.getTransactionType();
            case GROUP_BY_ID -> String.valueOf(t.getUserId());
            default -> "Общее";
        };
    }

    private AggregateResultDto performAggregation(List<Transaction> list, AggregateOption option,
                                                  LocalDateTime start, LocalDateTime end) {
        double totalSum = 0;
        long totalCount = 0;

        for (Transaction t : list) {
            long hits = 1;
            if (t instanceof RecurrentTransaction rt) {
                hits = rt.countOccurrencesBetween(start, end);
            }
            double amount = getProcessedAmount(t);

            totalSum += amount * hits;
            totalCount += hits;
        }

        AggregateResultDto dto = new AggregateResultDto();
        switch (option) {
            case SUM_COUNT -> dto.setSum(totalSum);
            case AMOUNT_COUNT -> dto.setCount(totalCount);
            case AVERAGE_VALUE_COUNT -> dto.setAverage(totalCount > 0 ? totalSum / totalCount : 0);
        }
        return dto;
    }

    public List<Transaction> readAll() {
        return repository.readAll();
    }

        private double getProcessedAmount(Transaction t) {
            if (t instanceof TaxableTransaction taxable) {
                return taxable.calculateTax().doubleValue();
            }

            if (t instanceof ForeignCurrencyTransaction foreign) {
                return foreign.convertToBaseCurrency(BigDecimal.valueOf(t.getAmount())).doubleValue();
            }
            return t.getAmount();
        }


    }