package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.repository.UserInfoRepository;
import com.example.card_test_app.security.service.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;
    private final UserInfoService userInfoService;


    public CardController(CardService cardService, UserInfoService userInfoService) {
        this.cardService = cardService;
        this.userInfoService = userInfoService;
    }

    @PostMapping("/createCard")
    public CardDto createCard(@RequestBody CreateCardRequest request){
        return cardService.createCard(request);
    }

    @GetMapping("/userCards")
    public Page<CardDto> getCards(
            @RequestParam Long userId,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false)LocalDate cardValidityPeriod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ){
        UserInfo user = userInfoService.getUserById(userId).get();
        user.setId(userId);

        return cardService.findCardsForUser(user, cardNumber, cardValidityPeriod, page, size);
    }
}
