package com.fintracker.data.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс управляющий транзакциями с иностранными валютами.
 */
public class ForeignCurrencyTransaction extends Transaction implements CurrencyConvertible {

    private double exchangeRate;

    public ForeignCurrencyTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                      double amount, String transactionType, double exchangeRate) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.exchangeRate = exchangeRate;
    }


    @Override
    public BigDecimal convertToBaseCurrency(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(exchangeRate));
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d: %.2f (Курс: %.2f)",
                getTransactionId(), getAmount(), exchangeRate);
    }
}
