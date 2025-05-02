package com.example.card_test_app.card.model.exceptions;

public class AuthenticateUserException extends RuntimeException{

    public AuthenticateUserException() {
    }

    public AuthenticateUserException(String message) {
        super(message);
    }

    public AuthenticateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
