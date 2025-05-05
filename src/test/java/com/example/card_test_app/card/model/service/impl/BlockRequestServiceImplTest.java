package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.enums.RequestBlockingStatus;
import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.card.model.exceptions.AuthenticateUserException;
import com.example.card_test_app.card.model.exceptions.CardNotFoundException;
import com.example.card_test_app.card.model.exceptions.RequestBlockNotFoundException;
import com.example.card_test_app.card.model.exceptions.UserNotFoundException;
import com.example.card_test_app.card.model.repository.BlockRequestRepository;
import com.example.card_test_app.card.model.repository.CardRepository;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.security.model.BlockRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import com.example.card_test_app.security.service.UserInfoDetails;
import com.example.card_test_app.security.service.UserInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class BlockRequestServiceImplTest {

    @Autowired
    BlockRequestRepository blockRequestRepository;

    @Autowired
    CardService cardService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    BlockRequestService blockRequestService;

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
        blockRequestRepository.deleteAll();
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
    @DisplayName("create block request success")
    void createBlockRequestSuccess() {
        List<Card> allCards = cardRepository.findAll();
        List<Card> cards = findActiveCard(allCards);
        Long cardId = cards.get(0).getId();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();
        Long userId = user.getId();

        BlockRequestDto request = new BlockRequestDto();
        request.setCardId(cardId);
        request.setUserId(userId);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        BlockRequest result = blockRequestService.createBlockRequest(request);
        assertNotNull(result);
        List<BlockRequest> blockRequests = blockRequestRepository.findByRequestBlockingStatus(RequestBlockingStatus.PENDING);
        assertEquals(1, blockRequests.size());
    }

    @Test
    @DisplayName("create block request with user not authenticate")
    void createBlockRequestWithUserNotAuthenticate() {
        BlockRequestDto request = createBlockRequest();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        user.setEmail("Otkly4ka@mail.com");

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Exception exception = assertThrows(AuthenticateUserException.class, () -> {
            blockRequestService.createBlockRequest(request);
        });

        assertEquals("User not authenticate", exception.getMessage());
    }

    @Test
    @DisplayName("get pending requests success")
    void getPendingRequest() {
        BlockRequest request = new BlockRequest();

        List<Card> allCards = cardRepository.findAll();
        List<Card> cards = findActiveCard(allCards);
        Long cardId = cards.get(0).getId();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        request.setCardId(cardId);
        request.setUserId(user.getId());
        request.setRequestTime(LocalDateTime.now());
        request.setRequestBlockingStatus(RequestBlockingStatus.PENDING);

        blockRequestRepository.save(request);

        List<BlockRequest> blockRequests = blockRequestService.getPendingRequest();
        assertFalse(blockRequests.isEmpty());
    }

    @Test
    @DisplayName("get pending requests with not found")
    void getPendingRequestWithNotFound() {

        Exception exception = assertThrows(RequestBlockNotFoundException.class, () -> {
            blockRequestService.getPendingRequest();
        });

        assertEquals("Blocking request not found", exception.getMessage());
    }

    @Test
    void approveBlockRequest() {
        BlockRequest request = new BlockRequest();

        List<Card> allCards = cardRepository.findAll();
        List<Card> cards = findActiveCard(allCards);
        Long cardId = cards.get(0).getId();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        request.setCardId(cardId);
        request.setUserId(user.getId());
        request.setRequestTime(LocalDateTime.now());
        request.setRequestBlockingStatus(RequestBlockingStatus.PENDING);

        blockRequestRepository.save(request);

        List<BlockRequest> blockRequests = blockRequestRepository.findAll();

        ApproveRequestDto approveRequestDto = new ApproveRequestDto();
        approveRequestDto.setRequestId(blockRequests.get(0).getId());
        approveRequestDto.setCardId(cardId);

        blockRequestService.approveBlockRequest(approveRequestDto);
        blockRequests = blockRequestRepository.findAll();

        assertEquals(RequestBlockingStatus.APPROVED, blockRequests.get(0).getRequestBlockingStatus());
    }

    @Test
    void approveBlockRequestWithCardNotFound() {
        BlockRequest request = new BlockRequest();

        Long cardId = 999L;
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        request.setCardId(cardId);
        request.setUserId(user.getId());
        request.setRequestTime(LocalDateTime.now());
        request.setRequestBlockingStatus(RequestBlockingStatus.PENDING);

        blockRequestRepository.save(request);

        List<BlockRequest> blockRequests = blockRequestRepository.findAll();

        ApproveRequestDto approveRequestDto = new ApproveRequestDto();
        approveRequestDto.setRequestId(blockRequests.get(0).getId());
        approveRequestDto.setCardId(cardId);

        Exception exception = assertThrows(CardNotFoundException.class, () -> {
            blockRequestService.approveBlockRequest(approveRequestDto);
        });

        assertEquals("Card not found with id: " + cardId, exception.getMessage());
    }

    @Test
    void approveBlockRequestWithBlockRequestNotFound() {
        BlockRequest request = new BlockRequest();

        List<Card> allCards = cardRepository.findAll();
        List<Card> cards = findActiveCard(allCards);
        Long cardId = cards.get(0).getId();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        ApproveRequestDto approveRequestDto = new ApproveRequestDto();
        approveRequestDto.setRequestId(999L);
        approveRequestDto.setCardId(cardId);

        Exception exception = assertThrows(RequestBlockNotFoundException.class, () -> {
            blockRequestService.approveBlockRequest(approveRequestDto);
        });

        assertEquals("Blocking request not found", exception.getMessage());
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

    private BlockRequestDto createBlockRequest(){
        List<Card> allCards = cardRepository.findAll();
        List<Card> cards = findActiveCard(allCards);
        Long cardId = cards.get(0).getId();
        UserInfo user = userInfoRepository.findByEmail("Pashech555@gmail.com").get();

        BlockRequestDto request = new BlockRequestDto();
        request.setCardId(cardId);
        request.setUserId(user.getId());

        return request;
    }
}