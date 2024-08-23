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
public class ChatGptController {
    private final ChatClient chatClient;

    @Autowired
    public ChatGptController(final ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("test")
    public String test() {
        return "Test";
    }

    @GetMapping("/call")
    public String call() {
        String cardInfo = "";
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
                        + "\nPlease Answer by Korean, 요체를 써서 공손하게 답변 작성.")
                .user(message)
                .call().content();
        return answer;
    }

}
