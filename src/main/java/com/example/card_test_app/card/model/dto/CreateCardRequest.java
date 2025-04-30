package com.example.card_test_app.card.model.dto;

import com.example.card_test_app.card.model.enums.Status;

import java.time.LocalDate;

public class CreateCardRequest {

    private String cardNumber;
    private LocalDate cardValidityPeriod;
    private Status status;
    private double balance;
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
