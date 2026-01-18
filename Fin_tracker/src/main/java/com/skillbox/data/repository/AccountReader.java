package com.skillbox.data.repository;

import com.skillbox.data.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, читающий информацию о счетах.
 */
public class AccountReader implements AccountRepository {

    private String filePath;

    public AccountReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Account> readAll() {
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    int accountId = Integer.parseInt(parts[0].trim());
                    int accountType = Integer.parseInt(parts[1].trim());
                    int userId = Integer.parseInt(parts[2].trim());
                    Account account = new Account(accountId, accountType, userId);
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }
}
