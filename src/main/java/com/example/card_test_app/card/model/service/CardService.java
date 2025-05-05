package com.example.card_test_app.card.model.service;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.security.model.TransferRequest;
import com.example.card_test_app.security.model.UserInfo;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface CardService {

    CardDto createCard(CreateCardRequest request);

    Page<CardDto> findCardsForUser(UserInfo userInfo, String cardNumber, LocalDate validityPeriod, int page, int size);

    void deleteCard(Long cardId);

    List<CardDto> getAllCards();

    Card findCardById(Long cardId);

    double getBalance(Long cardId);

    void activateCard(Long cardId);

    String transfer(TransferRequest transferRequest);
}
