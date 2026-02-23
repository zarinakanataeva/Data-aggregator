package com.fintracker;


import com.fintracker.controller.MainMenuController;
import com.fintracker.data.repository.*;
import com.fintracker.exception.IncorrectArgumentsNumberException;
import com.fintracker.service.TransactionService;

public class Application {

    public static void main(String[] args) {

        if (args.length < 3) {
            throw new IncorrectArgumentsNumberException(
                    "Неверное количество аргументов. Ожидается: 3.");
        }

        String accountFilename = args[0];
        String transactionFilename = args[1];
        String outputFilename = args[2];


        AccountRepository accountReader = new AccountReader(accountFilename);
        TransactionRepository transactionReader = new TransactionReader(transactionFilename);
        TransactionService transactionService = new TransactionService(transactionReader);
        AnalyticRepository saver = new AnalyticWriter(outputFilename);
        new MainMenuController(transactionService, saver).start();
    }
}
