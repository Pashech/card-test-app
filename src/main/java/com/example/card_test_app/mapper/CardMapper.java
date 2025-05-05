package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CardMapper {

    CardDto cardToCardDto(Card card);

    List<CardDto> cardsToCardDto(List<Card> cards);

    Page<CardDto> mapPageCardToCardDto(Page<Card> pageCard);
}
