package org.shds.smartpay.service;

import org.shds.smartpay.dto.CardRecommendDTO;
import org.shds.smartpay.dto.SellerDTO;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardBenefits;
import org.shds.smartpay.repository.CardBenefitsRepository;
import org.shds.smartpay.repository.CardRepository;
import org.shds.smartpay.repository.PayHistoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatGptServiceImpl implements ChatGptService {
    @Autowired
    private CardBenefitsRepository benefitsRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Autowired
    private ChatClient chatClient;

    @Override
    public CardRecommendDTO getCardBenefit(SellerDTO seller, String memberNo, int aiMode) {
        //MemberNo를 통해 보유하고 있는 카드 리스트 받아오기
        List<String> cardList = new ArrayList<>();

        if (aiMode == 0) {
            for (Card c : cardRepository.findByMemberNo(memberNo)) {
                cardList.add(c.getCardCode());
            }
        } else if (aiMode == 1){
            for (Object[] row : cardRepository.findByMemberNoGroupByPayInfoAndCardGoal1(memberNo)) {
                System.out.println("=============================================");
                System.out.println(row);
                System.out.println(row[13]);
                System.out.println(row[14]);
                System.out.println("=============================================");

                // totalPrice와 cardGoal1 값 가져오기 (정확한 타입으로 캐스팅)
                Object totalPriceObj = row[13]; // total_price
                Object cardGoal1Obj = row[14];  // card_goal1

                Integer totalPrice = null;
                Integer cardGoal1 = null;

                if (totalPriceObj != null) {
                    if (totalPriceObj instanceof Integer) {
                        totalPrice = (Integer) totalPriceObj;
                    } else if (totalPriceObj instanceof BigDecimal) {
                        totalPrice = ((BigDecimal) totalPriceObj).intValue();
                    }
                }

                if (cardGoal1Obj != null) {
                    if (cardGoal1Obj instanceof Integer) {
                        cardGoal1 = (Integer) cardGoal1Obj;
                    } else if (cardGoal1Obj instanceof BigDecimal) {
                        cardGoal1 = ((BigDecimal) cardGoal1Obj).intValue();
                    }
                }

                // totalPrice와 cardGoal1이 null이 아닌 경우에만 비교
                if (totalPrice != null && cardGoal1 != null && totalPrice < cardGoal1) {
                    String cardCode = (String) row[7];
                    System.out.println("=============================================");
                    System.out.println(cardCode);
                    System.out.println("=============================================");
                    cardList.add(cardCode);  // 조건을 만족하는 카드만 리스트에 추가
                }
            }
        } else if (aiMode == 2){
            for (Card c : cardRepository.findByMemberNo(memberNo)) {
                cardList.add(c.getCardCode());
            }
        }
//        System.out.println("카드리스트 조회 결과 ---------------");
//        System.out.println(cardList.toString());
        //임시로 테스트용 카드 코드 추가
//        cardList.add("13060013");
//        cardList.add("13060049");
//        cardList.add("03062657");

        //card_benefits 테이블에서 카드 리스트 뽑아오기, 등록된 모든 카드에 대한 혜택이 DB에 존재하지 않으면 null 리턴
        List<List<CardBenefits>> cardInfo = new ArrayList<>();
        for (String cardCode : cardList) {
            cardInfo.add(benefitsRepository.findAllByCardCode(cardCode));
        }
        if (cardInfo.isEmpty()) {
            //TODO: 기반 데이터 없이 GPT 검색으로 결과 리턴?
            //우선적으로 혜택 없음을 의미하기 위해 null 리턴
            return null;
        }
        //전월 실적 불러오기
        List<Map<String, Object>> previousSpendingList = payHistoryRepository.findPreviousSpendingByMemberNo(memberNo);
        //테스트용 더미값
//        for (int i = 1; i < cardList.size(); i++) {
//            previousSpending.add(i * 100000);
//        }
        StringBuilder previousSpending = new StringBuilder();
        for(Map<String, Object> key : previousSpendingList) {
            for(String cardCode : key.keySet()) {
                previousSpending.append(cardCode).append(":").append(key.get(cardCode)).append(", ");
            }
        }
        String message = "User cards: " + cardList.toString() +
                "\nPrevious month spending: " + previousSpending +
                "\nCurrent purchase: " + seller.getPurchasePrice() + "at" + seller.getFranchiseName() +
                "\nFranchise Code : " + seller.getFranchiseCode() +
                "\nRelevant card info: " + cardInfo.toString() +
                "\nBased on this information, which card would you recommend for this purchase? Provide your recommendation in the structured format specified.";
//        System.out.println(message);
        return chatClient.prompt().system("You are an AI assistant specializing in credit card recommendations. Analyze the user's card list, previous month's spending, and current purchase details to recommend the best card for maximum benefits. Provide your recommendation in the following structured class format:\n" +
                        "String recommendCard: [추천카드코드]\n" +
                        "int maximumBenefits: [Amount]\n" +
                        "(##The maximumBenefits follow this prompt : Check only if the value of previousSpending falls between spend_range_start and spend_range_end in the Relevant card info.\n" +
                        "Calculate the percentage based on either discount_rate or point_rate (whichever is present) from the Current purchase.\n" +
                        "If the value found in step 2 is less than max_discount or max_point, write the value from step 2 in the Amount column. Otherwise, write the max value in the Amount column.\n##)\n" +
                        "String benefitType: [적립/할인/캐시백]\n" +
                        "String explanation: [One sentence explanation of the benefit]\n" +
                        "String detailExplanation: [Detailed explanation of why this card is recommended]\n" +
                        "String caution: [Any conditions or limitations of the benefit]\n" +
                        "\nAlways consider the card's conditions and the user's spending patterns. Ensure all information is accurate based on the provided data. "
                        + "\nPlease Answer by Korean")
                .user(message)
                .call()
                .entity(CardRecommendDTO.class);
    }
}