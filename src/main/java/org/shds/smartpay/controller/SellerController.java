package org.shds.smartpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SellerController {

    @GetMapping("/seller")
    public String seller(
            @RequestParam(name = "email", required = false) String email
            , @RequestParam(name = "orderId", required = false) String orderId
            , Model model) {
        // email 쿼리 파라미터를 JSP에서 사용하기 위해 모델에 추가

        //ws에 판매자가 접속했음을 전달 == QR 생성 시점에 생긴 ws으로 접속

        model.addAttribute("email", email);
        model.addAttribute("orderId", orderId);
        return "seller";
    }

//    @PostMapping("/seller")
//    public String postSeller(@RequestBody SellerDTO) {
//        //ws에 판매자가 정보 입력 완료했음을 전달
//        //받아온 requestBody -> 결제하는 컨트롤러로 전달(/api/payment/ai)
//        //return 성공여부 처리 등 선택
//    }
}
