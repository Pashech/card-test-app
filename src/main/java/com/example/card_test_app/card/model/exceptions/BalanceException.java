package com.example.card_test_app.card.model.exceptions;

public class BalanceException extends RuntimeException {

    public BalanceException() {
    }

    public BalanceException(String message) {
        super(message);
    }

    public BalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
