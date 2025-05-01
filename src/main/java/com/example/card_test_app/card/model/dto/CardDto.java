package com.example.card_test_app.card.model.dto;

import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.security.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class CardDto {

    private String cardNumber;
    private UserDto cardOwner;
    private LocalDate cardValidityPeriod;
    private Status status;
    private double balance;

    public CardDto() {
    }

    public CardDto(String cardNumber, UserDto cardOwner, LocalDate cardValidityPeriod, Status status, double balance) {
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardValidityPeriod = cardValidityPeriod;
        this.status = status;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public UserDto getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(UserDto cardOwner) {
        this.cardOwner = cardOwner;
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
}
