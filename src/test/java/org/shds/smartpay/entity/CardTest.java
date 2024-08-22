package org.shds.smartpay.entity;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class CardTest {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void CardTest() {
        String[] test = new String[]{"03062376", "03062655", "03062657", "03062683", "03070115", "03090064", "03090092"};
        IntStream.rangeClosed(0, 6).forEach(i -> {
            Card card = Card.builder()
                    .cardNo("123456781234567" + i)
                    .cardNick("test카드" + i)
                    .cardPwd("test")
                    .validPeriod("12/24")
                    .regUser("테스터")
                    .cardCode(test[i])
                    .memberNo("testuser")
                    .build();
            cardRepository.save(card);
        });
    }
}