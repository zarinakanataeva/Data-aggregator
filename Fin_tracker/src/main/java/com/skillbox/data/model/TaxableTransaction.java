package com.skillbox.data.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Класс управляющий транзакциями, облагающихся налогом.
 */
public class TaxableTransaction extends Transaction implements Taxable {

    double taxRate;

    public TaxableTransaction(int userId, int transactionId, LocalDateTime date, String category,
                              double amount, String transactionType, double taxRate) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.taxRate = taxRate;
    }


    @Override
    public BigDecimal calculateTax() {
        BigDecimal amount = BigDecimal.valueOf(super.getAmount());
        BigDecimal tax = amount.multiply(BigDecimal.valueOf(taxRate));
        return amount.subtract(tax).setScale(2, java.math.RoundingMode.HALF_UP);
    }


    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d: %.2f (Налог: %.1f%%)",
                getTransactionId(), getAmount(), taxRate);
    }
}
