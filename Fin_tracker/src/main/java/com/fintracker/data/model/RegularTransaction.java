package com.fintracker.data.model;

import java.time.LocalDateTime;

/**
 * Класс управляющий обычными транзакциями.
 */
public class RegularTransaction extends Transaction {


    public RegularTransaction(int userId, int transactionId, LocalDateTime date,
                              String category, double amount, String transactionType) {
        super(userId, transactionId, date, category, amount, transactionType);
    }

    public String getTransactionInfo() {
        return String.format("Транзакция %d: %.2f",
                getTransactionId(), getAmount());
    }
}
