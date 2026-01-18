package com.skillbox.data.model;

import java.math.BigDecimal;

/**
 * Интерфейс описывает транзакции, совершенные в иностранной валюте
 */
public interface CurrencyConvertible {
    BigDecimal convertToBaseCurrency(BigDecimal amount);

}


