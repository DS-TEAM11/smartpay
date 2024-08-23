package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Objects;

@SpringBootTest
public class CardInfoRepositoryTests {

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Test
    public void findAllByCardCodeTest(String cardCode) {

        cardCode = "03010130";
    }

    // @Test
    // public void testCardInfo(){
    //     String cardCode = "03020724";
    //     String memberNo = "5da3a4ea-a155-4913-9d24-9d7012be570a";
    //     Object[] result = cardInfoRepository.getMemCardInfo(cardCode, memberNo);
    //     System.out.println(Arrays.toString(result));


    // }
}
