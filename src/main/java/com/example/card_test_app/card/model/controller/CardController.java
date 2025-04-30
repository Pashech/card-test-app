package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.service.CardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;


    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/createCard")
    public CardDto createCard(@RequestBody CardDto cardDto){
        return cardService.createCard(cardDto);
    }
}
