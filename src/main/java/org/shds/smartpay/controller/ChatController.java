package org.shds.smartpay.controller;

import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@ToString
@RestController
@Log4j2
public class ChatController {
    private final ChatClient chatClient;

    @Autowired
    public ChatController(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("test")
    public String test() {
        return "Test";
    }

    @GetMapping("/call")
    public String call() {
        String cardInfo = "{\n" +
                "  \"cardlist\": [\n" +
                "    {\n" +
                "      \"cardCompany\": \"신한카드\",\n" +
                "      \"cardName\": \"신한카드 Deep Dream\",\n" +
                "      \"isCredit\": \"true\",\n" +
                "      \"minimumMonthlyUsage\": 0,\n" +
                "      \"default_benefit\": \"0.7% 적립\",\n" +
                "      \"maximum_benefit\": \"3.0% 적립\",\n" +
                "      \"benefitLimit\": 20000,\n" +
                "      \"targetArea\": [\n" +
                "        \"모든 가맹점\",\n" +
                "        \"Dream 영역: 커피, 영화, 편의점, 서점, 베이커리\",\n" +
                "        \"추가 Dream 영역: 할인점, 백화점, 병원, 약국, 학원, 주유소\"\n" +
                "      ],\n" +
                "      \"cautions\": \"Dream 영역 및 추가 Dream 영역에서 최대 적립 한도는 각 5,000포인트\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cardCompany\": \"신한카드\",\n" +
                "      \"cardName\": \"신한카드 K-패스 체크\",\n" +
                "      \"isCredit\": \"false\",\n" +
                "      \"minimumMonthlyUsage\": 300000,\n" +
                "      \"default_benefit\": \"택시, 버스, 지하철 5% 할인\",\n" +
                "      \"maximum_benefit\": \"택시, 버스, 지하철 5% 할인\",\n" +
                "      \"benefitLimit\": 5000,\n" +
                "      \"targetArea\": [\n" +
                "        \"택시\",\n" +
                "        \"버스\",\n" +
                "        \"지하철\"\n" +
                "      ],\n" +
                "      \"cautions\": \"교통비 할인 한도는 월 5,000원\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        cardInfo += "{\n" +
                "  \"cardlist\": [\n" +
                "    {\n" +
                "      \"cardCompany\": \"신한카드\",\n" +
                "      \"cardName\": \"신한카드 The BEST-F\",\n" +
                "      \"isCredit\": \"true\",\n" +
                "      \"minimumMonthlyUsage\": 500000,\n" +
                "      \"default_benefit\": \"3% ~ 6% 적립\",\n" +
                "      \"maximum_benefit\": \"6% 적립\",\n" +
                "      \"benefitLimit\": 20000,\n" +
                "      \"targetArea\": [\n" +
                "        \"백화점\",\n" +
                "        \"대형마트\",\n" +
                "        \"온라인쇼핑\",\n" +
                "        \"주유소\",\n" +
                "        \"편의점\",\n" +
                "        \"커피전문점\",\n" +
                "        \"영화관\"\n" +
                "      ],\n" +
                "      \"cautions\": \"각 영역별 적립 한도는 월 5,000 포인트\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"cardCompany\": \"신한카드\",\n" +
                "      \"cardName\": \"신한카드 Deep Oil\",\n" +
                "      \"isCredit\": \"true\",\n" +
                "      \"minimumMonthlyUsage\": 300000,\n" +
                "      \"default_benefit\": \"주유비 리터당 60원 할인\",\n" +
                "      \"maximum_benefit\": \"주유비 리터당 60원 할인\",\n" +
                "      \"benefitLimit\": 20000,\n" +
                "      \"targetArea\": [\n" +
                "        \"주유소\"\n" +
                "      ],\n" +
                "      \"cautions\": \"리터당 할인은 LPG 포함\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String userCards = "신한 딥드립 신용" + ", 신한 K패스 체크" + ", 신한 Mr.Life 신용";
//        int[] previousSpending = new int[]{214000, 12312000, 0};
        String previousSpending = "214000, 12312000, 0";
        int purchaseAmount = 125000;
        String merchant = "롯데마트";
        String message = "User cards: " + userCards +
                "\nPrevious month spending: " + previousSpending +
                "\nCurrent purchase: " + purchaseAmount + "at" + merchant +
                "\nRelevant card info: " + cardInfo +
                "\nBased on this information, which card would you recommend for this purchase? Provide your recommendation in the structured format specified.";
        String answer = chatClient.prompt()
                .system("You are an AI assistant specializing in credit card recommendations. Analyze the user's card list, previous month's spending, and current purchase details to recommend the best card for maximum benefits. Provide your recommendation in the following structured format:\n" +
                        "1. 최대 혜택 금액: [Amount in 원]\n" +
                        "2. 혜택 방식: [적립/할인]\n" +
                        "3. 요약: [One sentence explanation of the benefit]\n" +
                        "4. 근거: [Detailed explanation of why this card is recommended]\n" +
                        "5. 유의사항: [Any conditions or limitations of the benefit]\n" +
                        "\nAlways consider the card's conditions and the user's spending patterns. Ensure all information is accurate based on the provided data. "
                        + "\nPlease Answer by Korean")
                .user(message)
                .call().content();
        return answer;
    }

}
