package com.example.card_test_app.card.model.repository;

import com.example.card_test_app.card.model.enums.RequestBlockingStatus;
import com.example.card_test_app.security.model.BlockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockRequestRepository extends JpaRepository<BlockRequest, Long> {

    List<BlockRequest> findByRequestBlockingStatus(RequestBlockingStatus requestBlockingStatus);
}
