package com.fintracker.data.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Абстрактный класс, представляющий собой транзакцию
 */
@Data
public abstract class Transaction {
    private final LocalDateTime date;
    private final BigDecimal amount;
    private final String category;
    private final int userId;
    private final String transactionType;
    private final int transactionId;

    protected Transaction(int userId, int transactionId, LocalDateTime date,
                          String category, BigDecimal amount, String transactionType) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.date = Objects.requireNonNull(date, "date must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.transactionType = Objects.requireNonNull(transactionType, "transactionType must not be null");
    }

    public abstract String getTransactionInfo();
}
