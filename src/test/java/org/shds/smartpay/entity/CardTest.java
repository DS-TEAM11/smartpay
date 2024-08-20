package org.shds.smartpay.entity;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.repository.CardRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardTest {

    @Autowired
    private CardRespository cardRespository;

    @Test
    public void CardTest(){
        Card card = Card.builder()
                .cardNo("5461117442875145")
                .cardNick("개인카드")
                .category("체크")
                .cardPwd(1234)
                .validPeriod("05/24")
                .regUser("양서윤")
                .cardCode(1)
                .memberNo(144)
                .build();

        cardRespository.save(card);
    }

}
