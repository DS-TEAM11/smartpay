package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class payHistoryRepositoryTests {

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(0, 10).forEach(i -> {
            History history = History.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .product("test" + i)
                    .price(10000)
                    .cardNo("1111-1111-1111-1111")
                    .cardCode("13060013")
                    .isAi(true)
                    .gptState(1)
                    .approval(0)
                    .payDate("20240701")
                    .savePrice(100)
                    .saveType(0)
                    .franchiseCode("10003")
                    .memberNo("testuser")
                    .build();
            System.out.println(payHistoryRepository.save(history));
        });
    }

    @Test
    public void findPreviousSpendingByMemberNo() {
        String memberNo = "testuser";
        System.out.println("결과");
        Map<String, Object> result = payHistoryRepository.findPreviousSpendingByMemberNo(memberNo);
        for (String key : result.keySet()) {
            System.out.println(key + ":" + result.get(key));
        }
    }
}
