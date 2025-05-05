package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.dto.CardDto;
import com.example.card_test_app.card.model.dto.CreateCardRequest;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.security.model.TransferRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/card")
@Tag(name = "Card controller", description = "Api для управления картами")
public class CardController {

    private final CardService cardService;
    private final UserInfoService userInfoService;


    public CardController(CardService cardService, UserInfoService userInfoService) {
        this.cardService = cardService;
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "Запрос на создание карты")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Карта успешно создана"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping("/createCard")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CreateCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request));
    }

    @Operation(summary = "Запрос на получение всех карт пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список карт пользователя успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/userCards")
    public ResponseEntity<Page<CardDto>> getCards(
            @RequestParam Long userId,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) LocalDate cardValidityPeriod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserInfo user = userInfoService.getUserById(userId);
        user.setId(userId);

        return ResponseEntity.ok(cardService.findCardsForUser(user, cardNumber, cardValidityPeriod, page, size));
    }

    @Operation(summary = "Запрос на получение всех карт")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список карт успешно получен")
    })
    @GetMapping("/allCards")
    public ResponseEntity<List<CardDto>> findAllCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @Operation(summary = "Запрос на получение баланса карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Баланс успешно получен"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @GetMapping("/balance/{cardId}")
    public ResponseEntity<Double> getCardBalance(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.getBalance(cardId));
    }

    @Operation(summary = "Запрос перевод денег с карты на карту пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно совершен"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена"),
            @ApiResponse(responseCode = "409", description = "Карта пользоваетеля заблокирована")
    })
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        return ResponseEntity.ok(cardService.transfer(transferRequest));
    }

    @Operation(summary = "Запрос на активирование карты пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно активирована")
    })
    @PutMapping("/activate/{cardId}")
    public ResponseEntity<Void> activateCard(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Запрос на удаление карты пользоваетля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Карта успешно удалена")
    })
    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
