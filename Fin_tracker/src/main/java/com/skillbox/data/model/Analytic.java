package com.skillbox.data.model;

import com.skillbox.controller.dto.AggregateResultDto;
import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.AggregateOption;
import com.skillbox.controller.option.GroupOption;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */
public class Analytic {
    private LocalDateTime calculationDate;
    private String groupOption;
    private String aggregateOption;
    private String filterDescription;
    private Map<String, Double> data; // Упростим для вывода: ключ -> число (сумма/среднее/кол-во)

    public Analytic(GroupOption group, AggregateOption aggregate, TransactionFilterDto filter, Map<String, AggregateResultDto> results) {
        this.calculationDate = LocalDateTime.now();
        this.groupOption = group.getName();
        this.aggregateOption = aggregate.getName();
        this.filterDescription = filter.toString();
        this.data = new java.util.LinkedHashMap<>();

        // Преобразуем сложные DTO в простые числа для итогового отчета
        results.forEach((key, value) -> {
            if (aggregate == AggregateOption.SUM_COUNT) data.put(key, value.getSum());
            else if (aggregate == AggregateOption.AMOUNT_COUNT) data.put(key, (double) value.getCount());
            else data.put(key, value.getAverage());
        });
    }

    // Геттеры нужны для Jackson (записи в JSON)
    public LocalDateTime getCalculationDate() { return calculationDate; }
    public String getGroupOption() { return groupOption; }
    public String getAggregateOption() { return aggregateOption; }
    public String getFilterDescription() { return filterDescription; }
    public Map<String, Double> getData() { return data; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===================================\n");
        sb.append("Дата: ").append(calculationDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Имя: '").append(groupOption).append(" (").append(aggregateOption).append(")'\n");
        sb.append("Фильтр: ").append(filterDescription).append("\n");
        sb.append("-----------------------------------\n");
        sb.append("Аналитика:\n");
        data.forEach((key, value) -> {
            if (aggregateOption.contains("количеств")) { // Проверка на метод агрегации
                sb.append(String.format("%s: %.0f\n", key, value));
            } else {
                sb.append(String.format("%s: %.2f\n", key, value));
            }
        });
        return sb.toString();
    }
}