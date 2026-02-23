package com.fintracker.data.model;


import java.util.List;

/**
 * Интерфейс для транзакций, к которым могут быть добавлены комментарии.
 */
public interface Commentable {
    List<String> getComments();

}
