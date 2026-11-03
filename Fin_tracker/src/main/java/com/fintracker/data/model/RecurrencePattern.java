package com.fintracker.data.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс, представляющий паттерн повторения транзакций.
 */
public enum RecurrencePattern {
    HOURLY("hourly", Duration.ofHours(1)),
    DAILY("daily", Period.ofDays(1)),
    WEEKLY("weekly", Period.ofWeeks(1)),
    BIWEEKLY("biweekly", Period.ofWeeks(2)),
    MONTHLY("monthly", Period.ofMonths(1)),
    YEARLY("yearly", Period.ofYears(1));

    private final String pattern;
    private final TemporalAmount amount;
    private static final Map<String, RecurrencePattern> MAP = Arrays.stream(values())
            .collect(Collectors.toMap(RecurrencePattern::getPattern, Function.identity()));

    RecurrencePattern(String pattern, TemporalAmount amount) {
        this.pattern = pattern;
        this.amount = amount;
    }

    public static RecurrencePattern of(String value) {
        if (value == null) return null;
        return MAP.get(value.trim().toLowerCase());
    }

    public String getPattern() { return pattern; }
    public TemporalAmount getDuration() { return amount; }
    public LocalDateTime addTo(LocalDateTime dateTime) { return dateTime.plus(amount); }
    public LocalDateTime subtractFrom(LocalDateTime dateTime) { return dateTime.minus(amount); }
}
