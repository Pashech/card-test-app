package com.example.card_test_app.mapper;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.UserDto;
import com.example.card_test_app.security.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardMapperImpl implements CardMapper {

    @Override
    public CardDto cardToCardDto(Card card) {

        CardDto dto = new CardDto();

            String masked = maskedString(card.getCardNumber());
            dto.setCardOwner(userInfoToUserDto(card.getCardOwner()));
            dto.setCardNumber(masked);
            dto.setCardValidityPeriod(card.getCardValidityPeriod());
            dto.setStatus(card.getStatus());
            dto.setBalance(card.getBalance());
        return dto;
    }

    protected UserDto userInfoToUserDto(UserInfo userInfo){
        if (userInfo == null) {
            return null;
        } else {
            UserDto userDto = new UserDto();
            userDto.setFirstName(userInfo.getFirstName());
            userDto.setLastName(userInfo.getLastName());
            return userDto;
        }
    }

    public List<CardDto> cardsToCardDto(List<Card> cards) {
        if ( cards == null ) {
            return null;
        }

        List<CardDto> list = new ArrayList<CardDto>( cards.size() );
        for ( Card card : cards ) {
            list.add( cardToCardDto( card ) );
        }

        return list;
    }

    public Page<CardDto> mapPageCardToCardDto(Page<Card> pageCard){
        return new PageImpl<>(pageCard.getContent().stream()
                .map(this::cardToCardDto)
                .collect(Collectors.toList()),
                pageCard.getPageable(),
                pageCard.getTotalElements()

        );
    }

    private String maskedString(String input){
        String[] parts = input.split(" ");
        StringBuilder masked = new StringBuilder();

        for(int i = 0; i < parts.length - 1; i++){
            masked.append("**** ");
        }
        masked.append(parts[parts.length - 1]);

        return masked.toString();
    }
}
