package com.example.card_test_app.card.model.service;

import com.example.card_test_app.security.model.BlockRequest;

import java.util.List;

public interface BlockRequestService {

    BlockRequest createBlockRequest(BlockRequest request);
    List<BlockRequest> getPendingRequest();
    void approveBlockRequest(Long requestId);
    void rejectBlockRequest(Long requestId);
}
