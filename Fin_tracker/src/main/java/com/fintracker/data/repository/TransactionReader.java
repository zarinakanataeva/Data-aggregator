package com.fintracker.data.repository;

import com.fintracker.data.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, читающий транзакции.
 */
public class TransactionReader implements TransactionRepository {

    private String filePath;

    public TransactionReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Transaction> readAll() {
        List<Transaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 6) {
                    continue;
                }

                int userId = Integer.parseInt(parts[0]);
                int transactionId = Integer.parseInt(parts[1]);
                LocalDateTime dateTime = LocalDateTime.parse(parts[2], formatter);
                String category = parts[3];
                double amount = Double.parseDouble(parts[4]);
                String transactionType = parts[5];

                Transaction transaction = null;

                switch (transactionType) {
                    case "Regular":
                        transaction = new RegularTransaction(userId, transactionId, dateTime, category, amount, transactionType);
                        break;
                    case "Taxable":
                        double taxRate = Double.parseDouble(parts[6]);
                        transaction = new TaxableTransaction(userId, transactionId, dateTime, category, amount, transactionType, taxRate);
                        break;
                    case "Recurrent":
                        String[] recurrentData = parts[6].split(";");

                        if (recurrentData.length < 2) {
                            System.err.println("Ошибка формата данных Recurrent: " + parts[6]);
                            continue;
                        }

                        String recurrencyPattern = recurrentData[0].trim();
                        int occurrences = Integer.parseInt(recurrentData[1].trim());

                        RecurrencePattern pattern = RecurrencePattern.of(recurrencyPattern);
                        transaction = new RecurrentTransaction(userId, transactionId, dateTime, category, amount, transactionType, pattern, occurrences);
                        break;

                    case "ForeignCurrency":
                        double exchangeRate = Double.parseDouble(parts[6]);
                        transaction = new ForeignCurrencyTransaction(userId, transactionId, dateTime, category, amount, transactionType, exchangeRate);
                        break;
                    case "Commentable":
                        String commentsStr = parts[6];
                        List<String> comments = new ArrayList<>();
                        for (String comment : commentsStr.split(";")) {
                            comments.add(comment.trim());
                        }
                        transaction = new CommentableTransaction(userId, transactionId, dateTime, category, amount, transactionType, comments);
                        break;
                    default:
                        continue;
                }
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}

