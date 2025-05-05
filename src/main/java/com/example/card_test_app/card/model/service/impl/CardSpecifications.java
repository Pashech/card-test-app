package com.example.card_test_app.card.model.service.impl;

import com.example.card_test_app.card.model.Card;
import com.example.card_test_app.security.model.UserInfo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CardSpecifications {

    public static Specification<Card> withCardOwnerAndFilters(UserInfo user, String cardNumber, LocalDate cardValidityPeriod) {

        return ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (user != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cardOwner"), user));
            }

            if (cardNumber != null && !cardNumber.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("cardNumber"), "%" + cardNumber + "%"));
            }

            if (cardValidityPeriod != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cardValidityPeriod"), cardValidityPeriod));
            }

            return predicate;
        });
    }
}
