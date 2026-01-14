package com.skillbox.controller.option;

public enum GroupOption implements MenuOption {

    BACK("вернуться назад (без группировки)"),
    GROUP_BY_MONTH("группировать по месяцам"),
    GROUP_BY_YEAR("группировать по годам"),
    GROUP_BY_DAY_OF_WEEK("группировать по дню недели"),
    GROUP_BY_CATEGORY("группировать по категории"),
    INCOME_OUTCOME_COUNT("считать доходы и расходы"),
    GROUP_BY_ACCOUNT_TYPE("группировать по типу счёта"),
    GROUP_BY_ID("группировать по ID пользователя");

    private final String name;

    GroupOption(String name) {
        this.name = name;
    }

    public static GroupOption of(int option) {
        return OptionUtils.of(GroupOption.class, option);
    }


    @Override
    public int getOption() { return ordinal();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
