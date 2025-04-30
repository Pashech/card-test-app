package com.example.card_test_app.card.model.repository;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.card.model.dto.CardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {


}
