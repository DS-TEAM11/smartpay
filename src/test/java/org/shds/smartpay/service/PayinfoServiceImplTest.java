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
    public void findByDateOrderByPayDate() {

        String startDate = null;
        String endDate = null;
        String memberNo = "acb944d7-2770-4eb5-a416-d9c1403601e0";
        String cardNo = null;

        List<PayInfoDTO> result = paymentService.findByDateOrderByPayDate(
            startDate
            , endDate
            , memberNo
            , cardNo
        );

        System.out.println("---------------------------");
        System.out.println(result);
        System.out.println("---------------------------");
    }
}
