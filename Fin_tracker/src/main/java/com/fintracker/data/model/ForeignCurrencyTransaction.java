package com.fintracker.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс управляющий транзакциями с иностранными валютами.
 */
public class ForeignCurrencyTransaction extends Transaction implements CurrencyConvertible {
    private final BigDecimal exchangeRate;

    public ForeignCurrencyTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                      BigDecimal amount, String transactionType, BigDecimal exchangeRate) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.exchangeRate = Objects.requireNonNull(exchangeRate, "exchangeRate must not be null");
        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Курс валюты должен быть больше нуля");
        }
    }

    public BigDecimal getExchangeRate() { return exchangeRate; }

    @Override
    public BigDecimal convertToBaseCurrency(BigDecimal amount) {
        return amount.multiply(exchangeRate).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d: %s (Курс: %s)",
                getTransactionId(), getAmount(), exchangeRate);
    }
}
