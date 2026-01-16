package com.skillbox;


import com.skillbox.controller.MainMenuController;
import com.skillbox.data.repository.*;
import com.skillbox.exception.IncorrectArgumentsNumberException;
import com.skillbox.service.TransactionService;

import java.io.File;

public class Application {

    public static void main(String[] args) {
        // проверка аргументов командной строки

        File f = new File("accounts.txt");
        System.out.println("Ищу файл здесь: " + f.getAbsolutePath());

        if (args.length < 3) {
            throw new IncorrectArgumentsNumberException(
                    "Неверное количество аргументов. Ожидается: 3.");
        }
        // имя входного файла с информацией об аккаунтах
        String accountFilename = args[0];
        // имя входного файла с информацией о транзакциях
        String transactionFilename = args[1];
        // имя выходного файла для записи результата
        String outputFilename = args[2];


        AccountRepository accountReader = new AccountReader(accountFilename);
        TransactionRepository transactionReader = new TransactionReader(transactionFilename);
        TransactionService transactionService = new TransactionService(transactionReader);
        AnalyticRepository saver = new AnalyticWriter(outputFilename);
        new MainMenuController(transactionService, saver).start();
    }
}
