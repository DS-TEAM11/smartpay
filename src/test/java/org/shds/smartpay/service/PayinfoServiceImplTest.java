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

        String payDate = "20240814";
        String memberNo = "UUID1";
        String codeNo = "1111-1111-1111-1111";

        List<PayInfoDTO> result = paymentService.findByDateOrderByPayDate(
            payDate
            , memberNo
            , codeNo
        );

        System.out.println("---------------------------");
        System.out.println(result);
        System.out.println("---------------------------");
    }


}
