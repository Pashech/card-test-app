package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.security.model.BlockRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block-requests")
public class BlockController {

    private final BlockRequestService blockRequestService;

    public BlockController(BlockRequestService blockRequestService) {
        this.blockRequestService = blockRequestService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BlockRequest>> getPendingRequests(){
        return ResponseEntity.ok(blockRequestService.getPendingRequest());
    }

    @PostMapping("/create-block-request")
    public ResponseEntity<BlockRequest> createBlockRequest(@Valid @RequestBody BlockRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(blockRequestService.createBlockRequest(request));
    }

    @PutMapping("/approve")
    public ResponseEntity<Void> approveBlockRequest(@Valid @RequestBody ApproveRequestDto approveRequestDto){
        blockRequestService.approveBlockRequest(approveRequestDto);
        return ResponseEntity.noContent().build();
    }
}
