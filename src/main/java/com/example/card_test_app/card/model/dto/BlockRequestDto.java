package com.example.card_test_app.card.model.dto;

public class BlockRequestDto {

    private Long requestId;
    private Long cardId;

    public BlockRequestDto() {
    }

    public BlockRequestDto(Long requestId, Long cardId) {
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
