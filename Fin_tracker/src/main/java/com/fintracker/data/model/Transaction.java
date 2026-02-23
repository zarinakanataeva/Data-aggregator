package com.fintracker.data.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий собой транзакцию
 */
@Data
public abstract class Transaction {

    protected Transaction(int userId, int transactionId, LocalDateTime date, String category, double amount, String transactionType) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    private LocalDateTime date;
    private double amount;
    private String category;
    private int userId;
    private String transactionType;
    private int transactionId;

    public abstract String getTransactionInfo();


}
