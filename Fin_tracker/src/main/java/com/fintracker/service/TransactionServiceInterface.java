package com.fintracker.service;

import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.data.model.Analytic;

/**
 * Интерфейс для обработки транзакций и расчета аналитических данных.
 */
public interface TransactionServiceInterface {

    /**
     * Вычисляет аналитические данные для заданных транзакций.
     *
     * @param transactionFilter объект фильтрации транзакций, содержащий параметры фильтра.
     * @param groupOption       опция для группировки транзакций.
     * @param aggregateOption   опция для агрегирования данных.
     * @return объект {@link Analytic}, содержащий результаты вычислений.
     * @throws IllegalArgumentException если любой из параметров равен null.
     */
    Analytic calculateAnalytics(TransactionFilterDto transactionFilter,
                                GroupOption groupOption,
                                AggregateOption aggregateOption);
}
