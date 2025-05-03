package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.security.model.BlockRequest;
import jakarta.validation.Valid;
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
    public List<BlockRequest> getPendingRequests(){
        return blockRequestService.getPendingRequest();
    }

    @PostMapping("/create-block-request")
    public BlockRequest createBlockRequest(@Valid @RequestBody BlockRequestDto request){
        return blockRequestService.createBlockRequest(request);
    }

    @PutMapping("/approve")
    public void approveBlockRequest(@Valid @RequestBody ApproveRequestDto approveRequestDto){
        blockRequestService.approveBlockRequest(approveRequestDto);
    }
}
