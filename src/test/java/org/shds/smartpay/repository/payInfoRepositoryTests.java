package org.shds.smartpay.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.PayInfo;
import org.shds.smartpay.entity.QPayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class payInfoRepositoryTests {
    @Autowired
    private PayInfoRepository payInfoRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 3).forEach(i->{
            PayInfo payInfo = PayInfo.builder()
                    .orderNo(UUID.randomUUID().toString())
                    .product("test"+i)
                    .price(1000)
                    .cardNo("1111-1111-1111-1111")
                    .cardCode("1")
                    .isAi(true)
                    .payDate("20240101")
                    .savePrice(100)
                    .saveType(1)
                    .franchiseCode("1")
                    .memberNo("UUID"+i)
                    .build();
            System.out.println(payInfoRepository.save(payInfo));
        });
    }

    @Test
    public void testSumSavePriceForCurrentMonth() {
        Long totalSavings = payInfoRepository.sumSavePriceForCurrentMonth(1); // type: save_type
        System.out.println("-------------------------------------");
        System.out.println(totalSavings);
        System.out.println("-------------------------------------");
    }

//    @Test
//    public void testFindByPayDateAndCardNo() {
//        String payDate = "20240804";
//        String cardNo = "1111-1111-1111-1111";
//
//        List<PayInfo> results = payInfoRepository.findByPayDateAndCardNo(payDate, cardNo);
//
//        System.out.println(results);
//        }
//    }

    @Test
    public void testFindByMemberNoOrderByPayDateDesc() {
        String memberNo = "UUID1";

        List<PayInfo> payInfos = payInfoRepository.findByMemberNoOrderByPayDateDesc(memberNo);

        System.out.println("------------------------------------------");
        System.out.println(payInfos);
        System.out.println("------------------------------------------");
    }
}
