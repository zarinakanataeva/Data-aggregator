package com.skillbox.data.model;

import java.util.List;

/**
 * Интерфейс для управления выписками по счету.
 */
public interface AccountStatement {
    List<Transaction> getTransactions();

}
