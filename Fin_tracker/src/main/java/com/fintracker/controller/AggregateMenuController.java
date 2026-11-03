package com.fintracker.controller;

import com.fintracker.controller.dto.AggregateResultDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.data.model.ForeignCurrencyTransaction;
import com.fintracker.data.model.TaxableTransaction;
import com.fintracker.data.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Консольный контроллер для управления агрегацией данных.
 */
public class AggregateMenuController extends AbstractMenuController<AggregateOption> {

    private List<Transaction> transactions;

    protected AggregateMenuController(List<Transaction> transactions) {
        super(AggregateOption.class, "Выберите тип агрегации");
        this.transactions = transactions;
    }

    public AggregateResultDto getAggregatedResult() {
        while (true) {
            AggregateOption option = selectMenu();
            switch (option) {
                case SUM_COUNT:
                    return calculateSum();
                case AVERAGE_VALUE_COUNT:
                    return calculateAverage();
                case AMOUNT_COUNT:
                    return transactionsCount();
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private AggregateResultDto calculateSum() {
        BigDecimal sum = transactions.stream()
                .map(this::getProcessedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        AggregateResultDto dto = new AggregateResultDto();
        dto.setSum(sum);
        return dto;
    }

    private AggregateResultDto calculateAverage() {
        BigDecimal total = transactions.stream()
                .map(this::getProcessedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal average = transactions.isEmpty() ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(transactions.size()), 2, java.math.RoundingMode.HALF_UP);
        AggregateResultDto dto = new AggregateResultDto();
        dto.setAverage(average);
        return dto;
    }

    private AggregateResultDto transactionsCount() {
        long count = transactions.size();
        AggregateResultDto dto = new AggregateResultDto();
        dto.setCount(count);
        return dto;
    }

    public AggregateOption getAggregatedResultOption() {
        return selectMenu();
    }

    private BigDecimal getProcessedAmount(Transaction t) {
        if (t instanceof TaxableTransaction taxable) {
            return taxable.calculateNetAmount();
        }
        if (t instanceof ForeignCurrencyTransaction foreign) {
            return foreign.convertToBaseCurrency(t.getAmount());
        }
        return t.getAmount();
    }

}
