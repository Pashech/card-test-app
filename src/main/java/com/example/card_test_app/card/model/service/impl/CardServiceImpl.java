package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.card.model.exceptions.*;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.mapper.CardMapper;
import com.example.card_test_app.security.model.TransferRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import com.example.card_test_app.security.service.UserInfoDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserInfoRepository userInfoRepository;
    private final CardMapper cardMapper;
    private final UserDetailsService userDetailsService;

    public CardServiceImpl(CardRepository cardRepository, UserInfoRepository userInfoRepository, CardMapper cardMapper, UserDetailsService userDetailsService) {
        this.cardRepository = cardRepository;
        this.userInfoRepository = userInfoRepository;
        this.cardMapper = cardMapper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public CardDto createCard(CreateCardRequest request) {

        UserInfo cardOwner = userInfoRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Card card = new Card();
        card.setCardNumber(request.getCardNumber());
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

        if (!email.equals(emailCurrentUser)) {
            throw new AuthenticateUserException("User not authenticate");
        }

        Pageable pageable = PageRequest.of(page, size);
        return cardMapper.mapPageCardToCardDto(cardRepository.findAll(CardSpecifications.withCardOwnerAndFilters(user, cardNumber, cardValidityPeriod), pageable));
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardMapper.cardsToCardDto(cardRepository.findAll());
    }

    @Override
    public Card findCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card not found with ID: " + cardId)
        );
    }

    @Override
    public double getBalance(Long cardId) {
        UserInfoDetails currentUser = (UserInfoDetails) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CardNotFoundException("Card not found with ID: " + cardId));
        String email = card.getCardOwner().getEmail();
        String userEmail = currentUser.getUsername();
        if (!email.equals(userEmail)) {
            throw new AuthenticateUserException("User not authenticated");
        }
        return card.getBalance();
    }

    @Override
    public void activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow();
        if (card.getStatus().equals(Status.BLOCKED)) {
            card.setStatus(Status.ACTIVE);
        }
        cardRepository.save(card);
    }

    @Transactional
    @Override
    public String transfer(TransferRequest transferRequest) {

        Card fromCard = cardRepository.findById(transferRequest.getFromCardId()).orElseThrow(
                () -> new CardNotFoundException("Card not found with ID: " + transferRequest.getFromCardId()));

        Card toCard = cardRepository.findById(transferRequest.getToCardId()).orElseThrow(
                () -> new CardNotFoundException("Card not found with ID: " + transferRequest.getToCardId()));

        if(fromCard.getStatus() == Status.BLOCKED || toCard.getStatus() == Status.BLOCKED){
            throw new CardBlockedException("Card is blocked");
        }

        if(fromCard.getBalance() < transferRequest.getAmount()){
            throw new BalanceException("Insufficient funds on the source card");
        }

        fromCard.setBalance(fromCard.getBalance() - transferRequest.getAmount());
        toCard.setBalance(toCard.getBalance() + transferRequest.getAmount());

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        return "Transfer successfully";
    }
}
