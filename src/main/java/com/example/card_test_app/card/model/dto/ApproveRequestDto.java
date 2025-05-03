package com.example.card_test_app.card.model.dto;

import jakarta.validation.constraints.NotNull;

public class ApproveRequestDto {

    @NotNull(message = "Request id is mandatory")
    private Long requestId;

    @NotNull(message = "Card id is mandatory")
    private Long cardId;

    public ApproveRequestDto() {
    }

    public ApproveRequestDto(Long requestId, Long cardId) {
        this.requestId = requestId;
        this.cardId = cardId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
