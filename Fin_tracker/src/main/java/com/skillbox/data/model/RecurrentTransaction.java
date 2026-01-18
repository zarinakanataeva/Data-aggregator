package com.skillbox.data.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public class RecurrentTransaction extends Transaction implements Recurring{
    /**
     * Класс управляющий повторяющимися транзакциями.
     */
    private RecurrencePattern pattern;
    private int occurrences;

    public RecurrentTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                double amount, String transactionType, RecurrencePattern pattern, int occurrences) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.pattern = pattern;
        this.occurrences = occurrences;
    }


    @Override
    public LocalDateTime getNextOccurrence(LocalDateTime dateTime) {
        return dateTime.plus(pattern.getDuration());
    }

    @Override
    public LocalDateTime getPreviousOccurrence(LocalDateTime dateTime) {
        return dateTime.minus(pattern.getDuration());
    }

    @Override
    public BigDecimal getTransactionAmount(LocalDateTime dateTime) {
        return
                BigDecimal.valueOf(getAmount()).multiply(BigDecimal.valueOf(occurrences));
    }

    @Override
    public boolean isExecutedBetween(LocalDateTime startDate, LocalDateTime endDate) {
       LocalDateTime current = getDate();
       for(int i = 0; i < occurrences; i++){
           if(current.isAfter(startDate) && current.isBefore(endDate)){
               return true;
           }
           current = getNextOccurrence(current);
       }
        return countOccurrencesBetween(startDate, endDate) > 0;
    }

    public long countOccurrencesBetween(LocalDateTime start, LocalDateTime end) {
        long count = 0;
        LocalDateTime current = getDate();

        for (int i = 0; i < occurrences; i++) {
            boolean notBeforeStart = (start == null || !current.isBefore(start));
            boolean notAfterEnd = (end == null || !current.isAfter(end));

            if (notBeforeStart && notAfterEnd) {
                count++;
            }
            current = getNextOccurrence(current);
            if (end != null && current.isAfter(end)) {
                break;
            }
        }
        return count;
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Recurrent Transaction: %s, Amount: %.2f, Pattern: %s, Occurrences: %d",
                getDate(), getAmount(), pattern, occurrences);
    }
}
