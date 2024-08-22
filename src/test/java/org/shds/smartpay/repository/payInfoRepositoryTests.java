package org.shds.smartpay.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.PayInfo;
import org.shds.smartpay.entity.QPayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public void SumSavePriceForCurrentMonthTest() {
        Long totalSavings = payInfoRepository.sumSavePriceForCurrentMonth(1); // type: save_type
        System.out.println("-------------------------------------");
        System.out.println(totalSavings);
        System.out.println("-------------------------------------");
    }

    @Test
    public void findByDateOrderByPayDate() {
        LocalDate now = LocalDate.now();
        LocalDate startLocalDate = now.minusWeeks(1);

        LocalDateTime startDateTime = startLocalDate.atStartOfDay();  // 시작 날짜의 시작 시간 (00:00:00)
        LocalDateTime endDateTime = now.atTime(LocalTime.MAX);        // 종료 날짜의 마지막 시간 (23:59:59)

        System.out.println("-------------------------------");
        System.out.println(startDateTime);
        System.out.println(endDateTime);
        System.out.println("-------------------------------");
        List<PayInfo> payInfos = payInfoRepository.findByDateOrderByPayDate(startDateTime, endDateTime);
        System.out.println(payInfos);
    }

    @Test
    public void findByMemberNoOrderByPayDateDescTest() {
        String memberNo = "UUID1";

        List<PayInfo> payInfos = payInfoRepository.findByMemberNoOrderByPayDateDesc(memberNo);

        System.out.println("------------------------------------------");
        System.out.println(payInfos);
        System.out.println("------------------------------------------");
    }

    @Test
    public void findByPayDateTest() {
        String input = "2024-08-04";
        String output = input.replace("-", "");

        System.out.println(output);


        List<PayInfo> payInfos = payInfoRepository.findByPayDate(output);

        System.out.println("---------------------------");
        System.out.println(payInfos);
        System.out.println("---------------------------");
    }
}
