package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.enums.RequestBlockingStatus;
import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.card.model.exceptions.CardNotFoundException;
import com.example.card_test_app.card.model.exceptions.RequestBlockNotFoundException;
import com.example.card_test_app.card.model.repository.BlockRequestRepository;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.security.model.BlockRequest;
import com.fasterxml.jackson.core.PrettyPrinter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockRequestServiceImpl implements BlockRequestService {

    private final BlockRequestRepository blockRequestRepository;
    private final CardService cardService;

    public BlockRequestServiceImpl(BlockRequestRepository blockRequestRepository, CardService cardService) {
        this.blockRequestRepository = blockRequestRepository;
        this.cardService = cardService;
    }

    @Override
    public BlockRequest createBlockRequest(BlockRequest request) {
        BlockRequest blockRequest = new BlockRequest();
        blockRequest.setCardId(request.getCardId());
        blockRequest.setUserId(request.getUserId());
        blockRequest.setRequestTime(LocalDateTime.now());
        blockRequest.setRequestBlockingStatus(RequestBlockingStatus.PENDING);

        return blockRequestRepository.save(blockRequest);
    }

    @Override
    public List<BlockRequest> getPendingRequest() {
        List<BlockRequest> byRequestBlockingStatus = blockRequestRepository.findByRequestBlockingStatus(RequestBlockingStatus.PENDING);
        if(byRequestBlockingStatus.isEmpty()){
            throw new RequestBlockNotFoundException("Blocking request not found");
        }
        return byRequestBlockingStatus;
    }

    @Override
    public void approveBlockRequest(BlockRequestDto blockRequestDto) {

        BlockRequest request = blockRequestRepository.findById(blockRequestDto.getRequestId())
                .orElseThrow(() -> new RequestBlockNotFoundException("Blocking request not found"));
        Card card = cardService.findCardById(blockRequestDto.getCardId());
        if(card == null){
            throw new CardNotFoundException("Card not found with id " + request.getCardId());
        }
        card.setStatus(Status.BLOCKED);
        request.setRequestBlockingStatus(RequestBlockingStatus.APPROVED);
        blockRequestRepository.save(request);
    }
}
