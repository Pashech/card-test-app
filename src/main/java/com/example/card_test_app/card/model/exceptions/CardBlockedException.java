package com.example.card_test_app.card.model.exceptions;

public class CardBlockedException extends RuntimeException {

    public CardBlockedException() {
    }

    public CardBlockedException(String message) {
        super(message);
    }

    public CardBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
