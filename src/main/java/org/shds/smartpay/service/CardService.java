package org.shds.smartpay.service;

import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.dto.CardDTO;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardInfo;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CardService {
    Optional<BinTableDTO> getCardCompanyByBin(String cardNumber);

    // 추가된 메서드: cardCompany로 카드 목록을 가져옴
    List<CardInfo> getCardsByCompany(String cardCompany);

    //memberNo로 카드 목록을 가져옴
    List<Card> getCardsByMemberNo(String memberNo);

    //card_code와 member_no로 카드 정보 조회
    Map<String, Object> getMemCardInfo(@RequestParam String cardNo, @RequestParam String memberNo);

    // update
    int updateByBenefitPriorityAndUsagePriority(List<CardDTO> cardDTOs);
}
