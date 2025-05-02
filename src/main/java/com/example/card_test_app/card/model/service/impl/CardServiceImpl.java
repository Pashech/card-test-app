package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.mapper.CardMapper;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import com.example.card_test_app.security.service.UserInfoDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserInfoRepository userInfoRepository;
    private final CardMapper cardMapper;
    private final EncryptService encryptService;
    private final UserDetailsService userDetailsService;

    public CardServiceImpl(CardRepository cardRepository, UserInfoRepository userInfoRepository, CardMapper cardMapper, EncryptService encryptService, UserDetailsService userDetailsService) {
        this.cardRepository = cardRepository;
        this.userInfoRepository = userInfoRepository;
        this.cardMapper = cardMapper;
        this.encryptService = encryptService;
        this.userDetailsService = userDetailsService;
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

        UserInfoDetails currentUser = (UserInfoDetails) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String emailCurrentUser = currentUser.getUsername();
        String email = user.getEmail();

        if(!email.equals(emailCurrentUser)){
            throw new IllegalArgumentException("Oooops");
        }

        Pageable pageable = PageRequest.of(page, size);
        return cardMapper.mapPageCardToCardDto(cardRepository.findAll(CardSpecifications.withCardOwnerAndFilters(user, cardNumber, cardValidityPeriod), pageable));
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardMapper.cardsToCardDto(cardRepository.findAll());
    }

    @Override
    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new NoSuchElementException("Card not found with ID: " + cardId)
        );
    }

    @Override
    public double getBalance(Long cardId) {
        UserInfoDetails currentUser = (UserInfoDetails) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Card card = cardRepository.findById(cardId).orElseThrow();
        String email = card.getCardOwner().getEmail();
        String userEmail = currentUser.getUsername();
        if(!email.equals(userEmail)){
            throw new IllegalArgumentException("Ooops");
        }
        return card.getBalance();
    }
}
