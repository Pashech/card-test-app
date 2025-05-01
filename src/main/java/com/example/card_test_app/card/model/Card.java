package com.example.card_test_app.card.model;

import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.security.model.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    @ManyToOne
    @JoinColumn(name = "card_owner_id")
    private UserInfo cardOwner;
    private LocalDate cardValidityPeriod;
    @Enumerated(EnumType.STRING)
    private Status status;
    private double balance;

    public Card() {
    }

    public Card(Long id, String cardNumber, UserInfo cardOwner, LocalDate cardValidityPeriod, Status status, double balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardValidityPeriod = cardValidityPeriod;
        this.status = status;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public UserInfo getCardOwner() {
        return cardOwner;
    }

    public void setCardOwner(UserInfo cardOwner) {
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
