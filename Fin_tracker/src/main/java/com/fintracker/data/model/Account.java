package com.fintracker.data.model;

import lombok.Data;

import java.util.Collections;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс представляющий собой счет в банке
 */
@Data
public class Account implements AccountInfo, BalanceOperations, AccountStatement {
    private final int accountId;
    private final int accountType;
    private final int userId;
    private final List<Transaction> transactions = new ArrayList<>();
    private BigDecimal balance = BigDecimal.ZERO;

    public Account(int accountId, int accountType, int userId) {
        if (AccountType.of(accountType) == null) {
            throw new IllegalArgumentException("Неизвестный тип счёта: " + accountType);
        }
        this.accountId = accountId;
        this.accountType = accountType;
        this.userId = userId;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.of(accountType);
    }

    @Override
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        if (transaction == null) throw new IllegalArgumentException("Транзакция не может быть null");
        if (transaction.getUserId() != userId) {
            throw new IllegalArgumentException("Нельзя добавить транзакцию другого пользователя в этот счёт");
        }
        transactions.add(transaction);
        BigDecimal amount = transaction.getAmount();
        if (transaction instanceof TaxableTransaction taxable) amount = taxable.calculateNetAmount();
        if (transaction instanceof ForeignCurrencyTransaction foreign) amount = foreign.convertToBaseCurrency(amount);
        balance = balance.add(amount);
    }
}