package com.example.card_test_app.card.model.controller;

import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.security.model.BlockRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block-requests")
public class BlockController {

    private final BlockRequestService blockRequestService;

    public BlockController(BlockRequestService blockRequestService) {
        this.blockRequestService = blockRequestService;
    }

    @PostMapping("/create-block-request")
    public BlockRequest createBlockRequest(@RequestBody BlockRequest request){
        return blockRequestService.createBlockRequest(request);
    }

    @GetMapping("/pending")
    public List<BlockRequest> getPendingRequests(){
        return blockRequestService.getPendingRequest();
    }

    @PutMapping("/{requestId}/approve")
    public void approveBlockRequest(@PathVariable Long requestId){
        blockRequestService.approveBlockRequest(requestId);
    }

    @PutMapping("/{requestId}/reject")
    public void rejectBlockRequest(@PathVariable Long requestId){
        blockRequestService.rejectBlockRequest(requestId);
    }


}
