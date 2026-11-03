package com.fintracker.data.repository;

import com.fintracker.data.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, читающий транзакции.
 */
public class TransactionReader implements TransactionRepository {
    private final Path filePath;

    public TransactionReader(String filePath) {
        this.filePath = Path.of(filePath);
    }

    @Override
    public List<Transaction> readAll() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank() || line.stripLeading().startsWith("#")) continue;
                try {
                    transactions.add(parseTransaction(parseCsvLine(line)));
                } catch (RuntimeException exception) {
                    throw new IllegalArgumentException("Ошибка в файле " + filePath + ", строка "
                            + lineNumber + ": " + exception.getMessage(), exception);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Не удалось прочитать файл транзакций: " + filePath, exception);
        }
        return transactions;
    }

    private Transaction parseTransaction(List<String> parts) {
        if (parts.size() < 6) throw new IllegalArgumentException("ожидается минимум 6 полей");
        int userId = Integer.parseInt(parts.get(0).trim());
        int transactionId = Integer.parseInt(parts.get(1).trim());
        LocalDateTime date = LocalDateTime.parse(parts.get(2).trim());
        String category = parts.get(3).trim();
        BigDecimal amount = new BigDecimal(parts.get(4).trim());
        String type = parts.get(5).trim();
        String details = parts.size() > 6 ? parts.get(6).trim() : "";

        return switch (type) {
            case "Regular" -> new RegularTransaction(userId, transactionId, date, category, amount, type);
            case "Taxable" -> new TaxableTransaction(userId, transactionId, date, category, amount, type,
                    new BigDecimal(required(details, "taxRate")));
            case "ForeignCurrency" -> new ForeignCurrencyTransaction(userId, transactionId, date, category,
                    amount, type, new BigDecimal(required(details, "exchangeRate")));
            case "Recurrent" -> parseRecurrent(userId, transactionId, date, category, amount, type, details);
            case "Commentable" -> new CommentableTransaction(userId, transactionId, date, category, amount,
                    type, details.isBlank() ? List.of() : splitComments(details));
            default -> throw new IllegalArgumentException("неизвестный тип транзакции: " + type);
        };
    }

    private RecurrentTransaction parseRecurrent(int userId, int id, LocalDateTime date, String category,
                                                BigDecimal amount, String type, String details) {
        String[] values = details.split(";", -1);
        if (values.length != 2) throw new IllegalArgumentException("для Recurrent ожидается pattern;occurrences");
        RecurrencePattern pattern = RecurrencePattern.of(values[0]);
        if (pattern == null) throw new IllegalArgumentException("неизвестная периодичность: " + values[0]);
        return new RecurrentTransaction(userId, id, date, category, amount, type, pattern,
                Integer.parseInt(values[1].trim()));
    }

    private String required(String value, String fieldName) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("не задано поле " + fieldName);
        return value;
    }

    private List<String> splitComments(String value) {
        return List.of(value.split(";", -1)).stream().map(String::trim).filter(s -> !s.isEmpty()).toList();
    }

    /** Разделяет CSV-строку, сохраняя запятые внутри кавычек и поддерживая двойные кавычки. */
    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (current == '"') {
                if (quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    quoted = !quoted;
                }
            } else if (current == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(current);
            }
        }
        if (quoted) throw new IllegalArgumentException("незакрытая кавычка в CSV");
        fields.add(field.toString());
        return fields;
    }
}

