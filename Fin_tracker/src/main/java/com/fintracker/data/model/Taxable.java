package com.fintracker.data.model;

import java.math.BigDecimal;

/**
 * Интерфейс описывает транзакции, из которых необходимо вычесть налоги,
 * например получение платы за услуги, получение доходов в виде дивидендов
 * и т.д.
 */
public interface Taxable {

    BigDecimal calculateTax();

}
