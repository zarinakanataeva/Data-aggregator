package com.skillbox.controller;

import com.skillbox.controller.option.GroupOption;
import com.skillbox.data.model.ForeignCurrencyTransaction;
import com.skillbox.data.model.TaxableTransaction;
import com.skillbox.data.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Консольный контроллер для группировки транзакций по опциям.
 */
public class GroupMenuController extends AbstractMenuController<GroupOption> {

    private List<Transaction> transactions;

    public GroupMenuController(List<Transaction> transactions) {
        super(GroupOption.class, "Выберите способ группировки");
        this.transactions = transactions;
    }

    public void groupTransactions() {
        while (true) {
            GroupOption option = selectMenu();
            switch (option) {
                case BACK:
                    return;
                case GROUP_BY_MONTH:
                    groupByMonth();
                    break;
                case GROUP_BY_YEAR:
                    groupByYear();
                    break;
                case GROUP_BY_DAY_OF_WEEK:
                    groupByDayOfWeek();
                    break;
                case GROUP_BY_CATEGORY:
                    groupByCategory();
                    break;
                case INCOME_OUTCOME_COUNT:
                    countIncomeOutcome();
                    break;
                case GROUP_BY_ACCOUNT_TYPE:
                    groupByAccountType();
                    break;
                case GROUP_BY_ID:
                    gropById();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private void groupByMonth() {
        Map<Integer, List<Transaction>> groupedByMonth =
                transactions.stream()
                        .collect(Collectors.groupingBy(
                                transaction -> transaction.getDate().getMonthValue()));
        System.out.println("Группировка по месяцам: " + groupedByMonth);
    }

    private void groupByYear() {
        Map<Integer, List<Transaction>> groupedByYear =
                transactions.stream().collect(Collectors.groupingBy(
                        transaction -> transaction.getDate().getYear()));
        System.out.println("Группировка по годам: " + groupedByYear);
    }

    private void groupByDayOfWeek() {
        Map<String, List<Transaction>> groupedByDayOfWeek =
                transactions.stream().collect(Collectors.groupingBy(
                        transaction -> transaction.getDate().getDayOfWeek().name()));
        System.out.println("Группировка по дням недели: " + groupedByDayOfWeek);
    }

    private void groupByCategory() {
        Map<String, List<Transaction>> groupedByCategory =
                transactions.stream().collect(Collectors.groupingBy(
                        Transaction::getCategory));
        System.out.println("Группировка по категориям: " + groupedByCategory);
    }


    private void countIncomeOutcome() {
        long incomeCount = transactions.stream()
                .filter(t -> getProcessedAmount(t) > 0)
                .count();
        long outcomeCount = transactions.stream()
                .filter(t -> getProcessedAmount(t) < 0)
                .count();
        System.out.println("Количество доходов: " + incomeCount);
        System.out.println("Количество расходов: " + outcomeCount);
    }

    private void groupByAccountType() {
        Map<String, List<Transaction>> groupedByAccountType =
                transactions.stream()
                        .collect(Collectors.groupingBy(transaction -> getAccountTypeName(transaction.getTransactionType())));
        System.out.println("Группировка по типу счета: " + groupedByAccountType);
    }


    private void gropById() {
        Map<Integer, List<Transaction>> groupedById = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getUserId));
        System.out.println("Группировка по ID пользователя: " + groupedById);

    }

    public GroupOption selectGroupOption() {
        return selectMenu();
    }

    public String getAccountTypeName(String accountType) {
        return switch (accountType) {
            case "0" -> "Текущий";
            case "1" -> "Сберегательный";
            case "2" -> "Кредитный";
            default -> "Неизвестный тип";
        };
    }

    private double getProcessedAmount(Transaction t) {
        if (t instanceof TaxableTransaction taxable) {
            return taxable.calculateTax().doubleValue();
        }
        if (t instanceof ForeignCurrencyTransaction foreign) {
            return foreign.convertToBaseCurrency(BigDecimal.valueOf(t.getAmount())).doubleValue();
        }
        return t.getAmount();
    }


}
