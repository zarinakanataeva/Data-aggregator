package com.fintracker.service;

import com.fintracker.controller.dto.AggregateResultDto;
import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import com.fintracker.data.model.*;
import com.fintracker.data.repository.TransactionRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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

    @Override
    public Analytic calculateAnalytics(TransactionFilterDto filter, GroupOption group, AggregateOption aggregate) {
        if (filter == null || group == null || aggregate == null) {
            throw new IllegalArgumentException("Фильтр, группировка и тип агрегации обязательны");
        }
        List<Transaction> filtered = readAll().stream().filter(filter.buildPredicate()).toList();
        Map<String, List<Transaction>> grouped = filtered.stream().collect(Collectors.groupingBy(
                transaction -> getGroupKey(transaction, group), LinkedHashMap::new, Collectors.toList()));
        Map<String, AggregateResultDto> results = new LinkedHashMap<>();
        grouped.forEach((key, transactions) -> results.put(key,
                performAggregation(transactions, filter.getStartDate(), filter.getEndDate())));
        return new Analytic(group, aggregate, filter, results);
    }

    private String getGroupKey(Transaction transaction, GroupOption option) {
        return switch (option) {
            case GROUP_BY_CATEGORY -> transaction.getCategory();
            case GROUP_BY_MONTH -> transaction.getDate().getYear() + "-" + String.format("%02d", transaction.getDate().getMonthValue());
            case GROUP_BY_YEAR -> String.valueOf(transaction.getDate().getYear());
            case GROUP_BY_DAY_OF_WEEK -> transaction.getDate().getDayOfWeek().name();
            case GROUP_BY_ID -> String.valueOf(transaction.getUserId());
            case INCOME_OUTCOME_COUNT -> transaction.getAmount().signum() >= 0 ? "Доходы" : "Расходы";
            // В исходном формате transactions.txt нет accountId, поэтому связать транзакцию со счётом нельзя.
            case GROUP_BY_ACCOUNT_TYPE -> "Тип счёта не указан в транзакции";
            default -> "Общее";
        };
    }

    private AggregateResultDto performAggregation(List<Transaction> transactions,
                                                  LocalDateTime start, LocalDateTime end) {
        BigDecimal sum = BigDecimal.ZERO;
        long count = 0;
        for (Transaction transaction : transactions) {
            long occurrences = transaction instanceof RecurrentTransaction recurring
                    ? recurring.countOccurrencesBetween(start, end) : 1;
            if (occurrences == 0) continue;
            sum = sum.add(processedAmount(transaction).multiply(BigDecimal.valueOf(occurrences)));
            count += occurrences;
        }

        AggregateResultDto result = new AggregateResultDto();
        result.setSum(sum.setScale(2, RoundingMode.HALF_UP));
        result.setCount(count);
        result.setAverage(count == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
        return result;
    }

    private BigDecimal processedAmount(Transaction transaction) {
        if (transaction instanceof TaxableTransaction taxable) return taxable.calculateNetAmount();
        if (transaction instanceof ForeignCurrencyTransaction foreign) {
            return foreign.convertToBaseCurrency(transaction.getAmount());
        }
        return transaction.getAmount();
    }

    public List<Transaction> readAll() {
        return repository.readAll();
    }
}
