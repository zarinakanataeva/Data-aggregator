package com.fintracker.controller.dto;

import com.fintracker.data.model.Commentable;
import com.fintracker.data.model.Recurring;
import com.fintracker.data.model.Transaction;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Класс для хранения фильтра по транзакциям.
 */

@Data
public class TransactionFilterDto {

    LocalDate startDate;
    LocalDate endDate;
    String commentToken;
    Double minAmount;
    Double maxAmount;
    String category;

    private Predicate<Transaction> datePredicate() {
        return transaction -> {
            LocalDateTime date = transaction.getDate();
            LocalDateTime start = getStartDate();
            LocalDateTime end = getEndDate();
            return (start == null || !date.isBefore(start)) &&
                    (end == null || !date.isAfter(end))
                    || (transaction instanceof Recurring && ((Recurring) transaction).isExecutedBetween(start, end));
        };
    }

    private Predicate<Transaction> commentPredicate() {
        if (commentToken == null || commentToken.isBlank()) {
            return transaction -> true;
        }
        return transaction -> transaction instanceof Commentable &&
                ((Commentable) transaction).getComments().stream()
                        .anyMatch(c -> c.toLowerCase().contains(commentToken.toLowerCase()));
    }

    private Predicate<Transaction> amountPredicate() {
        return transaction -> {
            double amount = transaction.getAmount();
            boolean isMinValid = minAmount == null || amount >= minAmount;
            boolean isMaxValid = maxAmount == null || amount <= maxAmount;
            return isMaxValid && isMinValid;
        };
    }


    private Predicate<Transaction> categoryPredicate() {
        if (category == null) {
            return transaction -> true;
        }
        return transaction -> transaction.getCategory().equals(category);
    }


    public Predicate<Transaction> buildPredicate() {
        return categoryPredicate()
                .and(amountPredicate())
                .and(commentPredicate())
                .and(datePredicate())
                .and(amountPredicate());
    }


    public LocalDateTime getStartDate() {
        return startDate == null ? null : startDate.atStartOfDay();
    }

    public LocalDateTime getEndDate() {
        return endDate == null ? null : endDate.atTime(23, 59, 59);
    }


    @Override
    public String toString() {
        return String.format("Категория: %s, Даты: %s — %s, Сумма: %s — %s, Комментарий: %s",
                category == null ? "Все" : category,
                startDate == null ? "..." : startDate,
                endDate == null ? "..." : endDate,
                minAmount == null ? "..." : minAmount,
                maxAmount == null ? "..." : maxAmount,
                commentToken == null ? "Все" : commentToken);
    }


}
