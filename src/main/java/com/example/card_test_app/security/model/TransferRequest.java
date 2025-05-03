package com.example.card_test_app.security.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

    @NotNull(message = "fromCard id is mandatory")
    private Long fromCardId;

    @NotNull(message = "toCardId is mandatory")
    private Long toCardId;

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount must be non-negative")
    private double amount;

    public TransferRequest() {
    }

    public TransferRequest(Long fromCardId, Long toCardId, double amount) {
        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
    }

    public Long getFromCardId() {
        return fromCardId;
    }

    public void setFromCardId(Long fromCardId) {
        this.fromCardId = fromCardId;
    }

    public Long getToCardId() {
        return toCardId;
    }

    public void setToCardId(Long toCardId) {
        this.toCardId = toCardId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
