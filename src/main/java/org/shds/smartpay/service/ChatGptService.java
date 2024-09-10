package org.shds.smartpay.service;

import org.shds.smartpay.dto.CardRecommendDTO;
import org.shds.smartpay.dto.SellerDTO;

import java.util.List;

public interface ChatGptService {
    //판매자, 구매자 정보를 받아서
    //구매자 정보(소유하고 있는 카드 리스트)를 통해 CardBenefits 테이블에서 혜택 받아오고
    //판매점 정보 + 구매자 정보 + 혜택 정보를 GPT에게 보내서 추천 정보 리턴
    CardRecommendDTO getCardBenefit(SellerDTO seller, String MemberNo, int aiMode);
}