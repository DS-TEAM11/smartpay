package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.PayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;


@SpringBootTest
public class payInfoRepositoryTests {
    @Autowired
    private PayInfoRepository payInfoRepository;

//
//    @Test
//    public void insertDummies(){
//        IntStream.rangeClosed(1, 3).forEach(i->{
//            PayInfo payInfo = PayInfo.builder()
//                    .orderNo(UUID.randomUUID().toString())
//                    .product("test"+i)
//                    .price(1000)
//                    .cardNo("1111-1111-1111-1111")
//                    .cardCode(1)
//                    .isAi(true)
//                    .payDate("20240101")
//                    .savePrice(100)
//                    .saveType(1)
//                    .franchiseNo(1)
//                    .memberNo("UUID"+i)
//                    .build();
//            System.out.println(payInfoRepository.save(payInfo));
//        });
//    }


}
