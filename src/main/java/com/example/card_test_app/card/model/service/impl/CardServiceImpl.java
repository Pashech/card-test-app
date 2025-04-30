package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.UserDto;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.mapper.CardMapper;
import com.example.card_test_app.mapper.UserMapper;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserInfoRepository userInfoRepository;
    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository cardRepository, UserInfoRepository userInfoRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userInfoRepository = userInfoRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public CardDto createCard(CardDto cardDto) {

        UserInfo cardOwner = userInfoRepository.findById(cardDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardOwner(cardOwner);
        card.setCardValidityPeriod(cardDto.getCardValidityPeriod());
        card.setStatus(cardDto.getStatus());
        card.setBalance(cardDto.getBalance());

        return cardMapper.cardToCardDto(cardRepository.save(card));
    }
}
