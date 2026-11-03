package com.fintracker.controller.dto;

import com.fintracker.data.model.Commentable;
import com.fintracker.data.model.Recurring;
import com.fintracker.data.model.Transaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * Класс для хранения фильтра по транзакциям.
 */

@Data
public class TransactionFilterDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String commentToken;
    private Double minAmount;
    private Double maxAmount;
    private String category;

    public Predicate<Transaction> buildPredicate() {
        validate();
        return datePredicate().and(amountPredicate()).and(commentPredicate()).and(categoryPredicate());
    }

    private void validate() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Начальная дата не может быть позже конечной");
        }
        if ((minAmount != null && !Double.isFinite(minAmount))
                || (maxAmount != null && !Double.isFinite(maxAmount))) {
            throw new IllegalArgumentException("Границы суммы должны быть конечными числами");
        }
        if (minAmount != null && maxAmount != null && minAmount > maxAmount) {
            throw new IllegalArgumentException("Минимальная сумма не может быть больше максимальной");
        }
    }

    private Predicate<Transaction> datePredicate() {
        LocalDateTime start = getStartDate();
        LocalDateTime endExclusive = endDate == null ? null : endDate.plusDays(1).atStartOfDay();
        return transaction -> {
            LocalDateTime date = transaction.getDate();
            boolean inRange = (start == null || !date.isBefore(start))
                    && (endExclusive == null || date.isBefore(endExclusive));
            return inRange || (transaction instanceof Recurring recurring
                    && recurring.isExecutedBetween(start, endExclusive == null ? null : endExclusive.minusNanos(1)));
        };
    }

    private Predicate<Transaction> amountPredicate() {
        BigDecimal min = minAmount == null ? null : BigDecimal.valueOf(minAmount);
        BigDecimal max = maxAmount == null ? null : BigDecimal.valueOf(maxAmount);
        return transaction -> (min == null || transaction.getAmount().compareTo(min) >= 0)
                && (max == null || transaction.getAmount().compareTo(max) <= 0);
    }

    private Predicate<Transaction> commentPredicate() {
        if (commentToken == null || commentToken.isBlank()) return transaction -> true;
        String token = commentToken.toLowerCase(Locale.ROOT);
        return transaction -> transaction instanceof Commentable commentable
                && commentable.getComments().stream().filter(c -> c != null)
                .anyMatch(comment -> comment.toLowerCase(Locale.ROOT).contains(token));
    }

    private Predicate<Transaction> categoryPredicate() {
        if (category == null || category.isBlank()) return transaction -> true;
        return transaction -> transaction.getCategory().equalsIgnoreCase(category.trim());
    }

    public LocalDateTime getStartDate() {
        return startDate == null ? null : startDate.atStartOfDay();
    }

    public LocalDateTime getEndDate() {
        return endDate == null ? null : endDate.atTime(23, 59, 59, 999_999_999);
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