package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.security.model.BlockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block-requests")
@Tag(name = "Block Requests", description = "API для управления запросами на блокировку")
public class BlockController {

    private final BlockRequestService blockRequestService;

    public BlockController(BlockRequestService blockRequestService) {
        this.blockRequestService = blockRequestService;
    }

    @Operation(summary = "Получить список ожидающих запросов на блокировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список ожидающих запросов успешно получен"),
            @ApiResponse(responseCode = "404", description = "Запрос на блокировку не найден")
    })
    @GetMapping("/pending")
    public ResponseEntity<List<BlockRequest>> getPendingRequests(){
        return ResponseEntity.ok(blockRequestService.getPendingRequest());
    }

    @Operation(summary = "Создать новый запрос на блокировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Запрос успешно создан"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    @PostMapping("/create-block-request")
    public ResponseEntity<BlockRequest> createBlockRequest(@Valid @RequestBody BlockRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(blockRequestService.createBlockRequest(request));
    }

    @Operation(summary = "Одобрить запрос на блокировку")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Запрос успешно одобрен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Запрос не найден"),
            @ApiResponse(responseCode = "404", description = "Карта не найдена")
    })
    @PutMapping("/approve")
    public ResponseEntity<Void> approveBlockRequest(@Valid @RequestBody ApproveRequestDto approveRequestDto){
        blockRequestService.approveBlockRequest(approveRequestDto);
        return ResponseEntity.noContent().build();
    }
}
