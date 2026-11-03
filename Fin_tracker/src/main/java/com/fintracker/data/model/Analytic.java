package com.fintracker.data.model;

import com.fintracker.controller.dto.AggregateResultDto;
import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */

@Data
public class Analytic {
    private final LocalDateTime calculationDate;
    private final String groupOption;
    private final String aggregateOption;
    private final String filterDescription;
    private final Map<String, AggregateResultDto> data;

    public Analytic(GroupOption group, AggregateOption aggregate,
                    TransactionFilterDto filter, Map<String, AggregateResultDto> results) {
        this.calculationDate = LocalDateTime.now();
        this.groupOption = group.getName();
        this.aggregateOption = aggregate.getName();
        this.filterDescription = filter.toString();
        this.data = new LinkedHashMap<>(results);
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("===================================\n")
                .append("Дата: ").append(calculationDate).append('\n')
                .append("Группировка: ").append(groupOption).append(" ( ").append(aggregateOption).append(" )\n")
                .append("Фильтр: ").append(filterDescription).append('\n')
                .append("-----------------------------------\nАналитика:\n");
        data.forEach((key, value) -> {
            output.append(key).append(": ");
            switch (aggregateOption) {
                case "подсчёт суммы" -> output.append(value.getSum());
                case "подсчёт среднего значения" -> output.append(value.getAverage());
                case "подсчёт количества" -> output.append(value.getCount());
                default -> output.append(value);
            }
            output.append('\n');
        });
        return output.toString();
    }
}
