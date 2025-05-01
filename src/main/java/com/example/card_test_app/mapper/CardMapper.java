package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "cardOwner", target = "cardOwner")
    CardDto cardToCardDto(Card card);

    @Mapping(source = "cardOwner", target = "cardOwner")
    Card cardDtoToCard(CardDto cardDto);

    default Page<CardDto> mapPageCardToCardDto(Page<Card> pageCard){
        return new PageImpl<>(pageCard.getContent().stream()
                .map(this::cardToCardDto)
                .collect(Collectors.toList()),
                pageCard.getPageable(),
                pageCard.getTotalElements()

        );
    }
}
