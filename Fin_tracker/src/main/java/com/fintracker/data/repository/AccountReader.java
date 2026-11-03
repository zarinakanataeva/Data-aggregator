
package com.fintracker.data.repository;

import com.fintracker.data.model.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Читает счета из файла формата:
 * accountId,accountType,userId
 */
public class AccountReader implements AccountRepository {

    private final Path filePath;

    public AccountReader(String filePath) {
        this.filePath = Path.of(filePath);
    }

    @Override
    public List<Account> readAll() {
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank() || line.stripLeading().startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length != 3) {
                    throw new IllegalArgumentException(
                            "Ошибка в файле " + filePath
                                    + ", строка " + lineNumber
                                    + ": ожидается 3 поля accountId,accountType,userId"
                    );
                }

                try {
                    int accountId = Integer.parseInt(parts[0].trim());
                    int accountType = Integer.parseInt(parts[1].trim());
                    int userId = Integer.parseInt(parts[2].trim());

                    accounts.add(new Account(
                            accountId,
                            accountType,
                            userId
                    ));

                } catch (RuntimeException exception) {
                    throw new IllegalArgumentException(
                            "Некорректные данные счёта в строке " + lineNumber,
                            exception
                    );
                }
            }

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Не удалось прочитать файл счетов: " + filePath,
                    exception
            );
        }

        return accounts;
    }
}
