package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.mapper.CardMapper;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserInfoRepository userInfoRepository;
    private final CardMapper cardMapper;
    private final EncryptService encryptService;

    public CardServiceImpl(CardRepository cardRepository, UserInfoRepository userInfoRepository, CardMapper cardMapper, EncryptService encryptService) {
        this.cardRepository = cardRepository;
        this.userInfoRepository = userInfoRepository;
        this.cardMapper = cardMapper;
        this.encryptService = encryptService;
    }

    @Override
    public CardDto createCard(CreateCardRequest request) {

        UserInfo cardOwner = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();
        try {
            card.setCardNumber(encryptService.encrypt(request.getCardNumber()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        card.setCardOwner(cardOwner);
        card.setCardValidityPeriod(request.getCardValidityPeriod());
        card.setStatus(request.getStatus());
        card.setBalance(request.getBalance());

        return cardMapper.cardToCardDto(cardRepository.save(card));
    }

    @Override
    public Page<CardDto> findCardsForUser(UserInfo user, String cardNumber, LocalDate cardValidityPeriod, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cardMapper.mapPageCardToCardDto(cardRepository.findAll(CardSpecifications.withCardOwnerAndFilters(user, cardNumber, cardValidityPeriod), pageable));
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardMapper.cardsToCardDto(cardRepository.findAll());
    }

    @Override
    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow();
    }
}
