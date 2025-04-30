package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "cardOwner", target = "cardOwner")
    CardDto cardToCardDto(Card card);

    @Mapping(source = "cardOwner", target = "cardOwner")
    Card cardDtoToCard(CardDto cardDto);
}
