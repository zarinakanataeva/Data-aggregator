package com.skillbox.controller.option;

/**
 * Перечисление для агрегации по категориям
 */
public enum AggregateOption implements MenuOption {
    SUM_COUNT("подсчёт суммы"),
    AVERAGE_VALUE_COUNT("подсчёт среднего значения"),
    AMOUNT_COUNT("подсчёт количества");

    private final String name;

    AggregateOption(String name) {
        this.name = name;
    }

    public static AggregateOption of(int option) {
        return OptionUtils.of(AggregateOption.class, option);
    }

    @Override
    public int getOption() {
        return ordinal();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
