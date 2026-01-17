package com.skillbox.controller;

import com.skillbox.controller.dto.TransactionFilterDto;
import com.skillbox.controller.option.SearchOption;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Консольный контроллер для управления навигацией по функционалу поиска транзакций.
 */
public class SearchMenuController extends AbstractMenuController<SearchOption> {

    public SearchMenuController() {
        super(SearchOption.class, "Выберите способ поиска транзакции");
    }

    public TransactionFilterDto getTransactionFilter() {
        TransactionFilterDto filter = new TransactionFilterDto();
        while (true) {
            SearchOption option = selectMenu();
            switch (option) {
                case EXIT:
                    return filter;
                case ALL_TRANSACTION:
                    return new TransactionFilterDto();
                case SEARCH_BY_CATEGORY:
                    filter = inputCategory(filter);
                    break;
                case SEARCH_BY_DATES:
                    filter = inputDates(filter);
                    break;
                case SEARCH_BY_AMOUNT:
                    filter = inputAmount(filter);
                    break;
                case SEARCH_BY_COMMENT:
                    filter = inputComment(filter);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + option);
            }
        }
    }

    private TransactionFilterDto inputComment(TransactionFilterDto filter) {
        System.out.println("Введите комментарий для поиска (или оставьте поле пустым): ");
        String comment = scanner.nextLine().trim();
        if (!comment.isEmpty()) {
            filter.setCommentToken(comment);
        }
        return filter;
    }

    private TransactionFilterDto inputAmount(TransactionFilterDto filter) {

        System.out.println("Введите минимальную сумму транзакции (Enter - не ограничивать сумму): ");
        String minAmountInput = scanner.nextLine().trim();
        if (!minAmountInput.isEmpty()) {
            try {
                double minAmount = Double.parseDouble(minAmountInput);
                filter.setMinAmount(minAmount);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод минимальной суммы.");
            }
        }
        System.out.println("Введите максимальную сумму транзакции(Enter - не ограничивать сумму): ");
        String maxAmountInput = scanner.nextLine();
        if (!maxAmountInput.isEmpty()) {
            try {
                double maxAmount = Double.parseDouble(maxAmountInput);
                filter.setMaxAmount(maxAmount);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод максимальной суммы.");
            }
        }
        return filter;
    }

    private TransactionFilterDto inputDates(TransactionFilterDto filter) {
        System.out.println("Введите начальную дату (в формате YYYY-MM-DD) (Enter - не ограничивать): ");
       String startDateInput = scanner.nextLine();
       if(!startDateInput.isEmpty()){
           LocalDate startDate = LocalDate.parse(startDateInput);
           filter.setStartDate(startDate);
       }
        System.out.println("Введите конечную дату (в формате YYYY-MM-DD) (Enter - не ограничивать): ");
   String endDateInput = scanner.nextLine();
   if(!endDateInput.isEmpty()){
       LocalDate endDate = LocalDate.parse(endDateInput);
       filter.setEndDate(endDate);
   }
   return filter;
    }

    private TransactionFilterDto inputCategory(TransactionFilterDto filter) {
        System.out.println("Введите категорию для поиска: (Enter - не задавать категорию)");
       String category = scanner.nextLine().trim();
       if(!category.isEmpty()){
           filter.setCategory(category);
       }
        return filter;
    }
}
