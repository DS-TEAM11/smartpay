package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardInfoRepositoryTests {

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Test
    public void findAllByCardCodeTest(String cardCode) {

        cardCode = "03010130";
    }
}
