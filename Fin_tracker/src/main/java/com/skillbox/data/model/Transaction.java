package com.skillbox.data.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий собой транзакцию
 */
// TODO: Реализуйте абстрактный класс
public abstract class Transaction  {

    protected Transaction(int userId, int transactionId, LocalDateTime date, String category,double amount, String transactionType ){
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.userId = userId;
        this. transactionType = transactionType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private LocalDateTime date;
    private double amount;
    private String category;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String accountType) {
        this.transactionType = accountType;
    }

    private int userId;
    private String transactionType;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    private int transactionId;

    public abstract String getTransactionInfo();


}
