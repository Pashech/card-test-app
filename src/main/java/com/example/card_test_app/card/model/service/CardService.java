package com.example.card_test_app.card.model.service;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;

public interface CardService {

    CardDto createCard(CreateCardRequest request);
}
