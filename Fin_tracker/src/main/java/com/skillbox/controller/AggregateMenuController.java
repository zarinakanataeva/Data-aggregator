package com.skillbox.controller;

import com.skillbox.controller.dto.AggregateResultDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.data.model.ForeignCurrencyTransaction;
import com.skillbox.data.model.TaxableTransaction;
import com.skillbox.data.model.Transaction;

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
        double sum = transactions.stream()
                .mapToDouble(this::getProcessedAmount)
                .sum();
        AggregateResultDto dto = new AggregateResultDto();
        dto.setSum(sum);
        return dto;
    }

    private AggregateResultDto calculateAverage() {
        double average = transactions.stream()
                .mapToDouble(this::getProcessedAmount)
                .average()
                .orElse(0.0);
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



