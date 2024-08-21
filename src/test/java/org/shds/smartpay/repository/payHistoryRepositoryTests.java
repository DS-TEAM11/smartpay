package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.History;
import org.shds.smartpay.entity.PayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class payHistoryRepositoryTests {

    @Autowired
    private PayHistoryRepository payHistoryRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(5, 5).forEach(i->{
            History history = History.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .product("test"+i)
                    .price(1000)
                    .cardNo("1111-1111-1111-1111")
                    .cardCode(4)
                    .isAi(false)
                    .gptState(1)
                    .approval(0)
                    .payDate("20240101")
                    .savePrice(100)
                    .saveType(0)
                    .franchiseNo(1)
                    .memberNo("UUID"+i)
                    .build();
            System.out.println(payHistoryRepository.save(history));
        });
    }
}
