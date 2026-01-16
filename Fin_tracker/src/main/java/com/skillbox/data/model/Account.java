package com.skillbox.data.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
// TODO: Исправьте этот класс, он не должен быть абстрактным
public class Account implements AccountInfo, BalanceOperations, AccountStatement {

    int accountId;
    int accountType;
    int userId;
    private List<Transaction> transactions; // Список транзакций
    private BigDecimal balance;

    public Account(int accountId, int accountType, int userId) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO; // Начальный баланс
    }


    @Override
    public int getAccountId() {
        return accountId;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.of(accountType);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public void addTransaction(Transaction transaction) {
transactions.add(transaction);
    }
}
