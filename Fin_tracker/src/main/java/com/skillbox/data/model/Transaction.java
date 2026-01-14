package com.skillbox.data.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Абстрактный класс, представляющий собой транзакцию
 */
// TODO: Реализуйте абстрактный класс
public abstract class Transaction  {

    protected Transaction(LocalDateTime date, double amount, String category, String accountType, int userId){
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.userId = userId;
        this. accountType = accountType;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    private int userId;
    private String accountType;

    public abstract String getTransactionInfo();


}
