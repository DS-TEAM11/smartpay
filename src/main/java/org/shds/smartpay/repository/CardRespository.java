package org.shds.smartpay.repository;

import org.shds.smartpay.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRespository extends JpaRepository<Card, String> {

    // 카드번호로 카드 조회
    Optional<Card> findByCardNo(String cardNo);



}
