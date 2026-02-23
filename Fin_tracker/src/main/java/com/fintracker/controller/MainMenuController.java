package com.fintracker.controller;

import com.fintracker.controller.dto.TransactionFilterDto;
import com.fintracker.controller.option.AggregateOption;
import com.fintracker.controller.option.GroupOption;
import com.fintracker.controller.option.MainMenuOption;
import com.fintracker.data.model.Analytic;
import com.fintracker.data.model.Transaction;
import com.fintracker.data.repository.AnalyticRepository;
import com.fintracker.service.TransactionService;

import java.util.List;

/**
 * Консольный контроллер для управления навигацией по главному меню.
 */
public class MainMenuController extends AbstractMenuController<MainMenuOption> {

    private final TransactionService transactionService;
    private final AnalyticRepository saver;
    private final SearchMenuController searchMenuController;

    public MainMenuController(TransactionService transactionService, AnalyticRepository saver) {
        super(MainMenuOption.class, "Анализ финансов");
        this.transactionService = transactionService;
        this.saver = saver;
        this.searchMenuController = new SearchMenuController();
    }

    public void start() {
        goMainMenu();
    }

    private void goMainMenu() {
        TransactionFilterDto transactionFilter = new TransactionFilterDto();
        GroupOption groupOption = GroupOption.BACK;
        AggregateOption aggregateOption = AggregateOption.AMOUNT_COUNT;
        Analytic analytics = null;

        List<Transaction> transactions = transactionService.readAll();

        while (true) {
            MainMenuOption i = selectMenu();
            switch (i) {
                case SEARCH_CRITERIA:
                    transactionFilter = searchMenuController.getTransactionFilter();
                    break;
                case GROUP_OPTION:
                    GroupMenuController gmc = new GroupMenuController(transactions);
                    groupOption = gmc.selectGroupOption();
                    break;
                case AGGREGATION_METHOD:
                    AggregateMenuController amc = new AggregateMenuController(transactions);
                    aggregateOption = amc.getAggregatedResultOption();
                    break;
                case CALCULATE_ANALYTICS:
                    analytics = transactionService.calculateAnalytics(transactionFilter,
                            groupOption, aggregateOption);
                    System.out.println(analytics);
                    break;
                case SAVE_ANALYTICS:
                    if (analytics == null) {
                        System.err.println("Необходимо сначала рассчитать аналитику");
                        break;
                    }
                    saver.save(analytics);
                    break;
                case EXIT:
                    return;
            }
        }
    }
}
