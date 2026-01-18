package com.skillbox;


import com.skillbox.controller.MainMenuController;
import com.skillbox.data.repository.*;
import com.skillbox.exception.IncorrectArgumentsNumberException;
import com.skillbox.service.TransactionService;

import java.io.File;

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
