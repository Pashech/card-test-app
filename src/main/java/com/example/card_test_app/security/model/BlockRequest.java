package com.example.card_test_app.security.model;

import com.example.card_test_app.card.model.enums.RequestBlockingStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "block-request")
public class BlockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long cardId;
    private Long userId;
    private LocalDateTime requestTime;
    @Enumerated(EnumType.STRING)
    private RequestBlockingStatus requestBlockingStatus;

    public BlockRequest() {
    }

    public BlockRequest(Long id, Long cardId, Long userId, LocalDateTime requestTime, RequestBlockingStatus requestBlockingStatus) {
        this.id = id;
        this.cardId = cardId;
        this.userId = userId;
        this.requestTime = requestTime;
        this.requestBlockingStatus = requestBlockingStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public RequestBlockingStatus getRequestBlockingStatus() {
        return requestBlockingStatus;
    }

    public void setRequestBlockingStatus(RequestBlockingStatus requestBlockingStatus) {
        this.requestBlockingStatus = requestBlockingStatus;
    }
}
