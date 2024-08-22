package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class payHistoryRepositoryTests {

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Test
    public void testRankList(){
        List<Object[]> result = payHistoryRepository.getCardRankList("전체");
        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }
    @Test
    public void insertDummies() {
        IntStream.rangeClosed(0, 5).forEach(i -> {
            History history = History.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .product("test" + i)
                    .price(152000)
                    .cardNo("1111-1111-1111-1111")
                    .cardCode("13060051")
                    .isAi(true)
                    .gptState(1)
                    .approval(0)
                    .payDate("20240701")
                    .savePrice(100)
                    .saveType(0)
                    .franchiseName("10003")
                    .memberNo("testuser")
                    .build();
            System.out.println(payHistoryRepository.save(history));
        });
    }

    @Test
    public void findPreviousSpendingByMemberNo() {
        String memberNo = "testuser";
        System.out.println("결과");
        List<Map<String, Object>> result = payHistoryRepository.findPreviousSpendingByMemberNo(memberNo);

        // 결과를 하나씩 출력
        for (Map<String, Object> map : result) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                System.out.println(key + ": " + value);
            }
            System.out.println("----");  // 각 Map의 끝을 구분하기 위한 구분선
        }
    }

}
