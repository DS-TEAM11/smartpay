package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.dto.CardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void findBymemberNo() {
        String memberNo = "test";

        List<Object[]> result = cardInfoRepository.findCardsWithCardInfo(memberNo);

        System.out.println("###################################");
        for (Object[] row : result) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("###################################");
    }

    @Test
    public void findBymemberNoAndcardCode() {
        String memberNo = "test";
        String cardNo = "4006966666666666";

        List<Object[]> result = cardInfoRepository.findBymemberNoAndcardCode(cardNo, memberNo);

        System.out.println("###################################");
        for (Object[] row : result) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("###################################");
    }
}
