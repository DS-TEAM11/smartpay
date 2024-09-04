package org.shds.smartpay.service;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.dto.PayInfoDTO;
import org.shds.smartpay.entity.PayInfo;
import org.shds.smartpay.repository.PayInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PayinfoServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void GetPayInfosByMemberNoTest() {

        String memberNo = "UUID1";

        List<PayInfo> result = paymentService.getPayInfosByMemberNo(memberNo);

        System.out.println("--------------------------");
        System.out.println(result);
        System.out.println("--------------------------");
    }

    @Test
    public void findByDateOrderByRegDate() {

        String startDate = "20240801";
        String endDate = "20240831";
        String memberNo = "test";
        String cardNo = "4221550025523553";

        List<PayInfoDTO> result = paymentService.findByDateOrderByRegDate(
            startDate
            , endDate
            , memberNo
            , cardNo
        );

        System.out.println("---------------------------");
        System.out.println(result);
        System.out.println("---------------------------");
    }

    @Test
    public void findByDateOrderByRegDatePage() {

        String startDate = "20240801";
        String endDate = "20240831";
        String memberNo = "test";
        String cardNo = "";
        int page = 1;
        int size = 10;

        List<PayInfoDTO> result = paymentService.findByDateOrderByRegDatePage(startDate, endDate, memberNo, cardNo, page, size);

        System.out.println("###########################################################");
        System.out.println(result);
        System.out.println(result.size());
        System.out.println("###########################################################");
    }
}
