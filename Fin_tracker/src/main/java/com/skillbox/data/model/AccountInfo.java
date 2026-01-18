package com.skillbox.data.model;

/**
 * Интерфейс для получения информации о счете.
 */
public interface AccountInfo {

    int getAccountId();

    int getUserId();

    AccountType getAccountType();
}
