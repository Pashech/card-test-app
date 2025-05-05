package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.card.model.exceptions.*;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.mapper.CardMapper;
import com.example.card_test_app.security.model.TransferRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CardServiceImpTestcontainers {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardServiceImpl cardService;

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @AfterEach
    public void cleanDb(){
        cardRepository.deleteAll();
        userInfoRepository.deleteAll();
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("Pashech555@gmail.com");
        userInfo.setFirstName("Pavel");
        userInfo.setLastName("Setsko");
        userInfo.setPassword("5908299");
        userInfo.setRoles("ROLE_USER");

        UserInfo userInfoAdmin = new UserInfo();
        userInfoAdmin.setEmail("Otkly4ka@mail.com");
        userInfoAdmin.setFirstName("Yana");
        userInfoAdmin.setLastName("Setsko");
        userInfoAdmin.setPassword("1840827");
        userInfoAdmin.setRoles("ROLE_ADMIN");

        userInfoRepository.save(userInfo);
        userInfoRepository.save(userInfoAdmin);

        Card card = new Card();
        card.setCardNumber("2222 2222 2222 2222");
        card.setCardOwner(userInfo);
        card.setCardValidityPeriod(LocalDate.of(2026, 12, 12));
        card.setStatus(Status.ACTIVE);
        card.setBalance(25000);

        Card cardTwo = new Card();
        cardTwo.setCardNumber("3333 3333 3333 3333");
        cardTwo.setCardOwner(userInfo);
        cardTwo.setCardValidityPeriod(LocalDate.of(2027, 12, 12));
        cardTwo.setStatus(Status.ACTIVE);
        cardTwo.setBalance(10000);

        Card cardThree = new Card();
        cardThree.setCardNumber("4444 4444 4444 4444");
        cardThree.setCardOwner(userInfo);
        cardThree.setCardValidityPeriod(LocalDate.of(2027, 12, 12));
        cardThree.setStatus(Status.BLOCKED);
        cardThree.setBalance(10000);

        cardRepository.save(card);
        cardRepository.save(cardTwo);
        cardRepository.save(cardThree);
    }

    @Test
    @DisplayName("Create card success")
    public void testCreateCardTest() {
        Optional<UserInfo> user = userInfoRepository.findByEmail("Pashech555@gmail.com");
        Long userActualId = user.get().getId();
        CreateCardRequest request = new CreateCardRequest();
        request.setUserId(userActualId);
        request.setCardNumber("1111 1111 1111 1111");
        request.setCardValidityPeriod(LocalDate.of(2025, 12,30));
        request.setStatus(Status.ACTIVE);
        request.setBalance(100.0);

        CardDto createdCardDto = cardService.createCard(request);

        assertNotNull(createdCardDto);

        assertFalse(cardRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Create card with user not found")
    public void testCreateCardTestNotFoundUser() {
        Long nonExistedUserId = 999L;
        CreateCardRequest request = new CreateCardRequest();
        request.setUserId(nonExistedUserId);
        request.setCardNumber("1111 1111 1111 1111");
        request.setCardValidityPeriod(LocalDate.of(2025, 12,30));
        request.setStatus(Status.ACTIVE);
        request.setBalance(100.0);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            cardService.createCard(request);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @DisplayName("Delete card success")
    void deleteCardTest() {
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());
        Long cardId = cards.get(0).getId();
        assertTrue(cardRepository.findById(cardId).isPresent());
        cardService.deleteCard(cardId);
        assertTrue(cardRepository.findById(cardId).isEmpty());
    }

    @Test
    @DisplayName("Delete card with card not found")
    void deleteCardTestNotFound() {
        Long nonExistedCardId = 999L;
        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.deleteCard(nonExistedCardId);
        });

        assertEquals("Card not found with id " + nonExistedCardId, exception.getMessage());
    }

    @Test
    @DisplayName("get all cards")
    void getAllCardsTest() {
        List<Card> cards = cardRepository.findAll();
        assertEquals(3, cards.size());
    }

    @Test
    @DisplayName("get card by id success")
    void findCardByIdTestSuccess() {
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());
        Long cardId = cards.get(0).getId();

        Card foundCard = cardService.findCardById(cardId);

        assertNotNull(foundCard);
        assertEquals(cardId, foundCard.getId());
    }

    @Test
    @DisplayName("get card by id with card not found")
    void findCardByIdTestWithCardNotFound() {

        Long nonExistedCardId = 999L;

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.findCardById(nonExistedCardId);
        });

        assertEquals("Card not found with id: " + nonExistedCardId, exception.getMessage());
    }

    @Test
    @DisplayName("get card`s balance success")
    void getBalanceSuccess() {
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());
        Long cardId = cards.get(0).getId();

        UserInfo userInfo = userInfoRepository.findByEmail("Pashech555@gmail.com").get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Card foundCard = cardService.findCardById(cardId);
        assertNotNull(foundCard);
        cardService.getBalance(foundCard.getId());
        assertTrue(foundCard.getBalance() > 0);
    }

    @Test
    @DisplayName("get card`s balance with Authenticate exception")
    void getBalanceWithCardWithAuthenticateException() {
        List<Card> cards = cardRepository.findAll();
        assertFalse(cards.isEmpty());
        Long cardId = cards.get(0).getId();

        UserInfo userInfo = userInfoRepository.findByEmail("Otkly4ka@mail.com").get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exception exception = assertThrows(AuthenticateUserException.class, () -> {
            cardService.getBalance(cardId);
        });

        assertEquals("User not authenticated", exception.getMessage());

    }

    @Test
    @DisplayName("get card`s balance with card not found")
    void getBalanceWithCardNotFound() {
        Long nonExistedCardId = 999L;

        UserInfo userInfo = userInfoRepository.findByEmail("Pashech555@gmail.com").get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.getBalance(nonExistedCardId);
        });

        assertEquals("Card not found with id: " + nonExistedCardId, exception.getMessage());
    }

    @Test
    @DisplayName("Activate card success")
    void activateCard() {
        List<Card> cards = cardRepository.findAll();
        Card card = findBlockedCard(cards);
        Long cardId = card.getId();

        cardService.activateCard(cardId);
        Card activatedCard = cardRepository.findById(cardId).get();
        assertEquals(Status.ACTIVE, activatedCard.getStatus());
    }

    @Test
    @DisplayName("Activate card with card not found")
    void activateCardWithCardNotFound() {
        Long nonExistedCardId = 999L;

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.activateCard(nonExistedCardId);
        });

        assertEquals("Card not found with id: " + nonExistedCardId, exception.getMessage());
    }

    @Test
    @DisplayName("Transfer money success")
    void transferSuccess() {
        List<Card> cards = cardRepository.findAll();
        List<Card> cardList = findActiveCard(cards);

        Long cardFromId = cardList.get(0).getId();
        Long cardToId = cardList.get(1).getId();

        TransferRequest request = new TransferRequest();
        request.setFromCardId(cardFromId);
        request.setToCardId(cardToId);
        request.setAmount(5000.0);

        String transfer = cardService.transfer(request);

        assertEquals("Transfer successfully", transfer);
    }

    @Test
    @DisplayName("Transfer money with card from not found")
    void transferWithCardFromNotFound() {
        List<Card> cards = cardRepository.findAll();
        List<Card> cardList = findActiveCard(cards);

        Long cardFromId = 999L;
        Long cardToId = cardList.get(1).getId();

        TransferRequest request = new TransferRequest();
        request.setFromCardId(cardFromId);
        request.setToCardId(cardToId);
        request.setAmount(5000.0);

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.transfer(request);
        });

        assertEquals("Card not found with id: " + cardFromId, exception.getMessage());
    }

    @Test
    @DisplayName("Transfer money with card to not found")
    void transferWithCardToNotFound() {
        List<Card> cards = cardRepository.findAll();
        List<Card> cardList = findActiveCard(cards);

        Long cardFromId = cardList.get(0).getId();
        Long cardToId = 999L;

        TransferRequest request = new TransferRequest();
        request.setFromCardId(cardFromId);
        request.setToCardId(cardToId);
        request.setAmount(5000.0);

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            cardService.transfer(request);
        });

        assertEquals("Card not found with id: " + cardToId, exception.getMessage());
    }

    @Test
    @DisplayName("Transfer money with card blocked")
    void transferWithCardBlocked() {
        List<Card> cards = cardRepository.findAll();
        List<Card> cardList = findActiveCard(cards);
        Card blockedCard = findBlockedCard(cards);

        Long blockedCardId = blockedCard.getId();
        Long cardToId = cardList.get(0).getId();

        TransferRequest request = new TransferRequest();
        request.setFromCardId(blockedCardId);
        request.setToCardId(cardToId);
        request.setAmount(5000.0);

        Exception exception = assertThrows(CardBlockedException.class, () -> {
            cardService.transfer(request);
        });

        assertEquals("Card is blocked", exception.getMessage());
    }

    @Test
    @DisplayName("Transfer money with card balance negative")
    void transferWithCardBalanceNegative() {
        List<Card> cards = cardRepository.findAll();
        List<Card> cardList = findActiveCard(cards);

        Long cardFrom = cardList.get(0).getId();
        Long cardToId = cardList.get(1).getId();

        TransferRequest request = new TransferRequest();
        request.setFromCardId(cardFrom);
        request.setToCardId(cardToId);
        request.setAmount(5000000.0);

        Exception exception = assertThrows(BalanceException.class, () -> {
            cardService.transfer(request);
        });

        assertEquals("Insufficient funds on the source card", exception.getMessage());
    }

    @Test
    @DisplayName("find cards for user success")
    void findCardsForUserTest(){

        UserInfo userInfo = userInfoRepository.findByEmail("Pashech555@gmail.com").get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Page<CardDto> result = cardService.findCardsForUser(userInfo, null, null, 0, 5);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("find cards for user with not authenticate")
    void findCardsForUserTestWithNotAuthentication(){

        UserInfo userInfo = userInfoRepository.findByEmail("Otkly4ka@mail.com").get();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userInfo.getEmail(), userInfo.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        userInfo.setEmail("Pashech555@gmail.com");

        Exception exception = assertThrows(AuthenticateUserException.class, () -> {
            cardService.findCardsForUser(userInfo, null, null, 0, 5);
        });

        assertEquals("User not authenticate", exception.getMessage());
    }

    private Card findBlockedCard(List<Card> cards){
        Card card = new Card();
        for(int i = 0; i < cards.size(); i++){
            if(cards.get(i).getStatus() == Status.BLOCKED){
                card = cards.get(i);
            }
        }
        return card;
    }

    private List<Card> findActiveCard(List<Card> cards){
        List<Card> cardList = new ArrayList<>();
        for (Card card : cards) {
            if (card.getStatus() == Status.ACTIVE) {
                cardList.add(card);
            }
        }
        return cardList;
    }
}
