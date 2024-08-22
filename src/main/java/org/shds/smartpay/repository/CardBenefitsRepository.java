package org.shds.smartpay.repository;

import org.shds.smartpay.entity.CardBenefits;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardBenefitsRepository extends JpaRepository<CardBenefits, String> {
    // card_code에 맞는 혜택 리스트 리턴
    List<CardBenefits> findAllByCardCode(String cardCode);
}
