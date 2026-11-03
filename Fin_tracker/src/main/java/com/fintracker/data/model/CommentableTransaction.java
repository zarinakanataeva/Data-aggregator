package com.fintracker.data.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Collections;

/**
 * Класс управляющий транзакциями с комментариями.
 */

public class CommentableTransaction extends Transaction implements Commentable {
    private final List<String> comments;

    public CommentableTransaction(int userId, int transactionId, LocalDateTime date, String category,
                                  BigDecimal amount, String transactionType, List<String> comments) {
        super(userId, transactionId, date, category, amount, transactionType);
        this.comments = comments == null ? new ArrayList<>() : new ArrayList<>(comments);
    }

    @Override
    public List<String> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void addComment(String comment) {
        if (comment == null || comment.isBlank())
            throw new IllegalArgumentException("Комментарий не может быть пустым");
        comments.add(comment.trim());
    }

    @Override
    public String getTransactionInfo() {
        return String.format("Транзакция %d (Комментарии: %s)", getTransactionId(), comments);
    }
}
