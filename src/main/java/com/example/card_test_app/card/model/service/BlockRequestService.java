package com.example.card_test_app.card.model.service;

import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.security.model.BlockRequest;

import java.util.List;

public interface BlockRequestService {

    BlockRequest createBlockRequest(BlockRequestDto request);
    List<BlockRequest> getPendingRequest();
    void approveBlockRequest(ApproveRequestDto request);
}
