package com.skillbox.data.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
// TODO: Исправьте этот класс, он не должен быть абстрактным
public class Account implements AccountInfo, BalanceOperations, AccountStatement {


    @Override
    public int getAccountId() {
        return 0;
    }

    @Override
    public int getUserId() {
        return 0;
    }

    @Override
    public AccountType getAccountType() {
        return null;
    }

    @Override
    public List<Transaction> getTransactions() {
        return List.of();
    }

    @Override
    public BigDecimal getBalance() {
        return null;
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }
}
