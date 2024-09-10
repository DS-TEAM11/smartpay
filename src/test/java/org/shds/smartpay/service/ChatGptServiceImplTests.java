package org.shds.smartpay.service;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.dto.SellerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatGptServiceImplTests {
    @Autowired
    private ChatGptService chatGptService;

    @Test
    public void responseTest() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setMemberNo("test@test.com");
        sellerDTO.setFranchiseName("GS25");
        sellerDTO.setFranchiseCode("10003");
        sellerDTO.setPurchasePrice(50000);
        sellerDTO.setPurchaseItems("돼지고기 3키로");
        String memberNo = "aaaaaa";
        int aiMode = 0;
        System.out.println(chatGptService.getCardBenefit(sellerDTO, memberNo, aiMode).toString());
    }
}