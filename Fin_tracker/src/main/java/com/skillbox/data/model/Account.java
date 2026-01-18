package com.skillbox.data.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
@Data
public class Account implements AccountInfo, BalanceOperations, AccountStatement {

    int accountId;
    int accountType;
    int userId;
    private List<Transaction> transactions;
    private BigDecimal balance;

    public Account(int accountId, int accountType, int userId) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.of(accountType);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
