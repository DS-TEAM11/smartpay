package org.shds.smartpay.service;

import org.shds.smartpay.dto.CardRecommendDTO;
import org.shds.smartpay.dto.SellerDTO;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardBenefits;
import org.shds.smartpay.repository.CardBenefitsRepository;
import org.shds.smartpay.repository.CardRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGptServiceImpl implements ChatGptService {
    @Autowired
    private CardBenefitsRepository benefitsRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ChatClient chatClient;

    @Override
    public CardRecommendDTO getCardBenefit(SellerDTO seller, String memberNo){
        //MemberNo를 통해 보유하고 있는 카드 리스트 받아오기
        List<String> cardList = new ArrayList<>();
        for(Card c : cardRepository.findByMemberNo(memberNo)) {
            cardList.add(c.getCardCode());
        }
        //임시로 테스트용 카드 코드 추가
        cardList.add("13060013");
        cardList.add("13060049");
        cardList.add("03062657");

        //card_benefits 테이블에 해당하는 카드 코드가 없는 경우
        List<List<CardBenefits>> cardInfo = new ArrayList<>();
        for(String cardCode : cardList) {
            cardInfo.add(benefitsRepository.findAllByCardCode(cardCode));
        }
        if(cardInfo.isEmpty()) {
            //TODO: 기반 데이터 없이 GPT 검색으로 결과 리턴?
            //우선적으로 혜택 없음을 의미하기 위해 null 리턴
            return null;
        }
        //전월 실적 불러오기
        /* SELECT SUM(price) as prespend, card_code
            FROM pay_history
            WHERE member_no = {member_no}
            AND mid(pay_date, 5,2) = mid(date_add(curdate(), interval -1 MONTH), 6,2)
            GROUP BY card_code;
         */
        //테스트 용도
        List<Integer> previousSpending = new ArrayList<>();
        for(int i = 1; i < cardList.size(); i++) {
            previousSpending.add(i * 100000);
        }
        String message = "User cards: " + cardList.toString() +
                "\nPrevious month spending: " + previousSpending +
                "\nCurrent purchase: " + seller.getPurchasePrice() + "at" + seller.getFranchiseName() +
                "\nFranchise Code : " + seller.getFranchiseCode() +
                "\nRelevant card info: " + cardInfo.toString() +
                "\nBased on this information, which card would you recommend for this purchase? Provide your recommendation in the structured format specified.";
//        String gptAnswer = chatClient.prompt().system("You are an AI assistant specializing in credit card recommendations. Analyze the user's card list, previous month's spending, and current purchase details to recommend the best card for maximum benefits. Provide your recommendation in the following structured format:\n" +
//                        "1. 최대 혜택 금액: [Amount in 원]\n" +
//                        "2. 혜택 방식: [적립/할인]\n" +
//                        "3. 요약: [One sentence explanation of the benefit]\n" +
//                        "4. 근거: [Detailed explanation of why this card is recommended]\n" +
//                        "5. 유의사항: [Any conditions or limitations of the benefit]\n" +
//                        "\nAlways consider the card's conditions and the user's spending patterns. Ensure all information is accurate based on the provided data. "
//                        + "\nPlease Answer by Korean")
//                .user(message)
//                .call()
//                .content();

        System.out.println(message);
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
