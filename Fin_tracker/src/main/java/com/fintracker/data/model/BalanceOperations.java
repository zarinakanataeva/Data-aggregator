package com.fintracker.data.model;

import java.math.BigDecimal;

/**
 * Интерфейс для операций с балансом счета.
 */
public interface BalanceOperations {
    BigDecimal getBalance();

    void addTransaction(Transaction transaction);
}
