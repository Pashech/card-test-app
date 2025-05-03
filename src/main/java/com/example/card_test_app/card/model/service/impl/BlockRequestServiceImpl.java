package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.ApproveRequestDto;
import com.example.card_test_app.card.model.dto.BlockRequestDto;
import com.example.card_test_app.card.model.enums.RequestBlockingStatus;
import com.example.card_test_app.card.model.enums.Status;
import com.example.card_test_app.card.model.exceptions.AuthenticateUserException;
import com.example.card_test_app.card.model.exceptions.CardNotFoundException;
import com.example.card_test_app.card.model.exceptions.RequestBlockNotFoundException;
import com.example.card_test_app.card.model.exceptions.UserNotFoundException;
import com.example.card_test_app.card.model.repository.BlockRequestRepository;
import com.example.card_test_app.card.model.service.BlockRequestService;
import com.example.card_test_app.card.model.service.CardService;
import com.example.card_test_app.security.model.BlockRequest;
import com.example.card_test_app.security.model.UserInfo;
import com.example.card_test_app.security.service.UserInfoDetails;
import com.example.card_test_app.security.service.UserInfoService;
import com.fasterxml.jackson.core.PrettyPrinter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlockRequestServiceImpl implements BlockRequestService {

    private final BlockRequestRepository blockRequestRepository;
    private final CardService cardService;
    private final UserDetailsService userDetailsService;
    private final UserInfoService userInfoService;

    public BlockRequestServiceImpl(BlockRequestRepository blockRequestRepository, CardService cardService, UserDetailsService userDetailsService, UserInfoService userInfoService) {
        this.blockRequestRepository = blockRequestRepository;
        this.cardService = cardService;
        this.userDetailsService = userDetailsService;
        this.userInfoService = userInfoService;
    }

    @Override
    public BlockRequest createBlockRequest(BlockRequestDto request) {
        BlockRequest blockRequest = new BlockRequest();
        blockRequest.setCardId(request.getCardId());
        Card card = cardService.findCardById(request.getCardId());
        if(card == null){
            throw new CardNotFoundException("Card not found with id " + request.getCardId());
        }
        blockRequest.setUserId(request.getUserId());
        UserInfoDetails currentUser = (UserInfoDetails) userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        UserInfo user = userInfoService.getUserById(request.getUserId());

        if (user == null) {
            throw new UserNotFoundException("User not found with id " + request.getUserId());
        }

        if(!currentUser.getUsername().equals(user.getEmail())){
            throw new AuthenticateUserException("User not authenticate");
        }

        blockRequest.setRequestTime(LocalDateTime.now());
        blockRequest.setRequestBlockingStatus(RequestBlockingStatus.PENDING);

        return blockRequestRepository.save(blockRequest);
    }

    @Override
    public List<BlockRequest> getPendingRequest() {
        List<BlockRequest> byRequestBlockingStatus = blockRequestRepository.findByRequestBlockingStatus(RequestBlockingStatus.PENDING);
        if (byRequestBlockingStatus.isEmpty()) {
            throw new RequestBlockNotFoundException("Blocking request not found");
        }
        return byRequestBlockingStatus;
    }

    @Override
    public void approveBlockRequest(ApproveRequestDto approveRequestDto) {

        BlockRequest request = blockRequestRepository.findById(approveRequestDto.getRequestId())
                .orElseThrow(() -> new RequestBlockNotFoundException("Blocking request not found"));
        Card card = cardService.findCardById(approveRequestDto.getCardId());
        if (card == null) {
            throw new CardNotFoundException("Card not found with id " + request.getCardId());
        }
        card.setStatus(Status.BLOCKED);
        request.setRequestBlockingStatus(RequestBlockingStatus.APPROVED);
        blockRequestRepository.save(request);
    }
}
