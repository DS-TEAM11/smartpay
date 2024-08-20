package org.shds.smartpay.service;

import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.entity.CardInfo;


import java.util.List;
import java.util.Optional;

public interface CardService {
    Optional<BinTableDTO> getCardCompanyByBin(String cardNumber);

    // 추가된 메서드: cardCompany로 카드 목록을 가져옴
    List<CardInfo> getCardsByCompany(String cardCompany);
}
