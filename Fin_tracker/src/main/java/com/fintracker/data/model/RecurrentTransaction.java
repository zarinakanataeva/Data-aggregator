package com.fintracker.data.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс управляющий повторяющимися транзакциями.
 */

public class RecurrentTransaction extends Transaction implements Recurring {
    private final RecurrencePattern pattern;
    private final int occurrences;

    public RecurrentTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                BigDecimal amount, String transactionType,
                                RecurrencePattern pattern, int occurrences) {
        super(userId, transactionId, date, category, amount, transactionType);
        if (pattern == null) throw new IllegalArgumentException("Периодичность не задана");
        if (occurrences < 1) throw new IllegalArgumentException("Количество повторений должно быть положительным");
        this.pattern = pattern;
        this.occurrences = occurrences;
    }

    public RecurrencePattern getPattern() { return pattern; }
    public int getOccurrences() { return occurrences; }

    @Override public LocalDateTime getNextOccurrence(LocalDateTime dateTime) { return pattern.addTo(dateTime); }
    @Override public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) { return pattern.subtractFrom(dateTime); }

    @Override
    public BigDecimal getTransactionAmount(LocalDateTime dateTime) {
        return getAmount().multiply(BigDecimal.valueOf(countOccurrencesBetween(dateTime, dateTime)));
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return countOccurrencesBetween(startDate, endDate) > 0;
    }

    public long countOccurrencesBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) return 0;
        long count = 0;
        LocalDateTime current = getDate();
        for (int i = 0; i < occurrences; i++) {
            boolean afterStart = start == null || !current.isBefore(start);
            boolean beforeEnd = end == null || !current.isAfter(end);
            if (afterStart && beforeEnd) count++;
            if (end != null && current.isAfter(end)) break;
            current = getNextOccurrence(current);
        }
        return count;
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Повторяющаяся транзакция: %s, сумма: %s, периодичность: %s, повторений: %d",
                getDate(), getAmount(), pattern, occurrences);
    }
}