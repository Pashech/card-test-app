package com.example.card_test_app.card.model.dto;

import jakarta.validation.constraints.NotNull;

public class BlockRequestDto {

    @NotNull(message = "Card ID is mandatory")
    private Long cardId;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    public BlockRequestDto() {
    }

    public BlockRequestDto(Long cardId, Long userId) {
        this.cardId = cardId;
        this.userId = userId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
