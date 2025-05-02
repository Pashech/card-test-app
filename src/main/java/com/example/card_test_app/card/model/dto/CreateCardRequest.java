package com.example.card_test_app.card.model.dto;

import com.example.card_test_app.card.model.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateCardRequest {

    @NotBlank(message = "Card number is mandatory")
    private String cardNumber;

    @Future(message = "Card validity period must be in the future")
    private LocalDate cardValidityPeriod;

    @NotNull(message = "Status is mandatory")
    private Status status;

    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be non-negative")
    private double balance;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    public CreateCardRequest() {
    }

    public CreateCardRequest(String cardNumber, LocalDate cardValidityPeriod, Status status, double balance, Long userId) {
        this.cardNumber = cardNumber;
        this.cardValidityPeriod = cardValidityPeriod;
        this.status = status;
        this.balance = balance;
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getCardValidityPeriod() {
        return cardValidityPeriod;
    }

    public void setCardValidityPeriod(LocalDate cardValidityPeriod) {
        this.cardValidityPeriod = cardValidityPeriod;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
