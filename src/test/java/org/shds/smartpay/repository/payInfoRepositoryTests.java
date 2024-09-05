package org.shds.smartpay.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.shds.smartpay.dto.MyStaticDTO;
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

        String startDate = "20240801";
        String endDate = "20240830";
        String memberNo = "acb944d7-2770-4eb5-a416-d9c1403601e0";
        System.out.println("-------------------------------");
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println("-------------------------------");
        List<PayInfo> payInfos = payInfoRepository.findByDateOrderByRegDate(startDate, endDate, memberNo);
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

    @Test
    public void test(){
        PayInfo payinfo = payInfoRepository.findByOrderNo("pay_info 테이블 저장");
        System.out.println(payinfo);
    }

    @Test
    public void findPageTest() {
        String memberNo = "test";
        String startDate = "20240801";
        String endDate = "20240831";
        int page = 3;
        int size = 10;
        List<PayInfo> result = payInfoRepository.findPayInfoByDateAndMemberNoWithPaging(startDate, endDate, memberNo, page, size);
        System.out.println("#############################################");
        System.out.println(result);
        System.out.println(result.size());
        System.out.println("#############################################");
    }

    @Test
    public void findMonthlySummaryTest() {
        String memberNo = "test";
        List<Object[]> result = payInfoRepository.getPaymentDetails(memberNo);

        System.out.println("###############################################");
        System.out.println(result);
        for (Object[] row: result) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("###############################################");
    }
}
