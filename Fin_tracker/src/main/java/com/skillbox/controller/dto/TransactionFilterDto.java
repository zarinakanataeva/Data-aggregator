package com.skillbox.controller.dto;

import com.skillbox.data.model.Commentable;
import com.skillbox.data.model.Recurring;
import com.skillbox.data.model.Transaction;
import jdk.jfr.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Класс для хранения фильтра по транзакциям.
 */
public class TransactionFilterDto {

    LocalDate startDate;
    LocalDate endDate;
    String commentToken;
    Double minAmount;
    Double maxAmount;
    String category;

    /**
     * Создает предикат для фильтрации транзакций по диапазону дат. Также вернет те Recurring транзакции, которые будут
     * или были выполнены в указанный диапазон дат
     *
     * @return Предикат для фильтрации транзакций по диапазону дат.
     */
    private Predicate<Transaction> datePredicate() {
        return transaction -> {
            LocalDateTime date = transaction.getDate();
            LocalDateTime start = startDate == null ? null : startDate.atStartOfDay();
            LocalDateTime end = endDate == null ? null : endDate.atStartOfDay();
            return (start == null || !date.isBefore(start)) &&
                    (end == null || !date.isAfter(end))
                    || (transaction instanceof Recurring && ((Recurring) transaction).isExecutedBetween(start, end));
        };
    }

    private Predicate<Transaction> commentPredicate() {
        if (commentToken == null || commentToken.isBlank()) {
            return transaction -> true;
        }
        // Поиск подстроки без учета регистра
        return transaction -> transaction instanceof Commentable &&
                ((Commentable) transaction).getComments().stream()
                        .anyMatch(c -> c.toLowerCase().contains(commentToken.toLowerCase()));
    }
    /**
     * Создает предикат для фильтрации транзакций по комментарию или его части. Фильтруются только транзакции,
     * имплементирующие интерфейс Commentable. Если токен пустой или null, то возвращается предикат, который всегда
     * вернет true
     *
     * @return Предикат для фильтрации транзакций по комментарию.
     */


    /**
     * Создает предикат для фильтрации транзакций по диапазону суммы.
     *
     * @return Предикат для фильтрации транзакций по диапазону суммы.
     */
    private Predicate<Transaction> amountPredicate() {
        // TODO: реализуйте метод, возвращающий предикат для фильтрации транзакций по диапазону суммы
        return transaction -> {
            double amount = transaction.getAmount();
            boolean isMinValid = minAmount == null || amount >= minAmount;
            boolean isMaxValid = maxAmount == null || amount <= maxAmount;
            return isMaxValid && isMinValid;
        };
    }

    /**
     * Создает предикат для фильтрации транзакций по категории.
     *
     * @return Предикат для фильтрации транзакций по категории.
     */
    private Predicate<Transaction> categoryPredicate() {
      if(category == null){
          return transaction -> true;
      }
        return transaction -> transaction.getCategory().equals(category);
    }

    /**
     * Собирает предикат для фильтрации транзакции.
     *
     * @return Предикат для фильтрации транзакции.
     */
    public Predicate<Transaction> buildPredicate() {
        return categoryPredicate()
                .and(amountPredicate())
                .and(commentPredicate())
                .and(datePredicate())
                .and(amountPredicate());
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setComment(String commentToken) {
        this.commentToken = commentToken;
    }

    public void setMinAmount(Double minAmount) {
        this.minAmount = minAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setCategory(String category) {
        this.category = category;
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
