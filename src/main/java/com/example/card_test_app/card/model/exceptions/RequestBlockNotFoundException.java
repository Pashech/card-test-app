package com.example.card_test_app.card.model.exceptions;

public class RequestBlockNotFoundException extends RuntimeException{

    public RequestBlockNotFoundException() {
    }

    public RequestBlockNotFoundException(String message) {
        super(message);
    }

    public RequestBlockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
