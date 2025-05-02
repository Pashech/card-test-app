package com.example.card_test_app.card.model.exceptions;

import lombok.extern.slf4j.Slf4j;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;


@ControllerAdvice
public class GlobalExceptionsHandler {

    private final Logger logger = Logger.getLogger("GlobalExceptionsHandler");

    @ExceptionHandler
    public ResponseEntity<AppError> catchUserNotFoundException(UserNotFoundException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchAuthenticateUserException(AuthenticateUserException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchCardNotFoundException(CardNotFoundException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchRequestBlockNotFoundException(RequestBlockNotFoundException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchCardBlockedException(CardBlockedException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchBalanceException(BalanceException exception){
        logger.info(exception.getMessage());

        return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), exception.getMessage()), HttpStatus.CONFLICT);
    }
}
