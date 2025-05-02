package com.example.card_test_app.card.model.exceptions;

public class CardNotFoundException extends RuntimeException{

    public CardNotFoundException() {
    }

    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
