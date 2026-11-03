package com.fintracker.data.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Класс управляющий обычными транзакциями.
 */
public class RegularTransaction extends Transaction {
    public RegularTransaction(int userId, int transactionId, LocalDateTime date,
                              String category, BigDecimal amount, String transactionType) {
        super(userId, transactionId, date, category, amount, transactionType);
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d: %s", getTransactionId(), getAmount());
    }
}