package com.fintracker.data.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс управляющий транзакциями, облагающихся налогом.
 */
public class TaxableTransaction extends Transaction implements Taxable {
    private final BigDecimal taxRate;

    public TaxableTransaction(int userId, int transactionId, LocalDateTime date, String category,
                              BigDecimal amount, String transactionType, BigDecimal taxRate) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.taxRate = Objects.requireNonNull(taxRate, "taxRate must not be null");
        if (taxRate.compareTo(BigDecimal.ZERO) < 0 || taxRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Налоговая ставка должна быть в диапазоне от 0 до 1");
        }
    }

    public BigDecimal getTaxRate() { return taxRate; }

    @Override
    public BigDecimal calculateNetAmount() {
        return getAmount().multiply(BigDecimal.ONE.subtract(taxRate)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d: %s (Налог: %s%%)",
                getTransactionId(), getAmount(), taxRate.multiply(BigDecimal.valueOf(100)));
    }
}
