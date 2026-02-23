package com.fintracker.data.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс управляющий транзакциями с комментариями.
 */


public class CommentableTransaction extends Transaction implements Commentable {

    private List<String> comments;

    public CommentableTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                  double amount, String transactionType, List<String> comments) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.comments = (comments != null) ? new ArrayList<>(comments) : new ArrayList<>();
    }

    @Override
    public List<String> getComments() {
        return comments;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d (Список: %s)",
                getTransactionId(), comments.toString());
    }
}
