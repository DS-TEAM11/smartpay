package org.shds.smartpay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.dto.*;
import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.entity.PayInfo;
import org.shds.smartpay.service.CardService;
import org.shds.smartpay.service.ChatGptService;
import org.shds.smartpay.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/payment/")
@Log4j2
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final ChatGptService chatGptService;
    private final CardService cardService;

    //첫번쨰 결제 로그 저장
    //(들어와야 하는 값: product, price, cardCode, isAi, gptState, payDate, franchiseName, franchiseCode, memberNo
    @PostMapping("/pay")
    public ResponseEntity<String> firstSavPayment(@RequestBody PayInfoDTO payInfoDTO) {
        String orderNo = paymentService.firstSaveHistory(payInfoDTO);
        System.out.println(orderNo);
        return ResponseEntity.ok(orderNo);
    }

    @PostMapping("/ai")
    public ResponseEntity<Object> receivePaymentRequest(@RequestBody SellerDTO sellerDTO, @RequestParam String memberNo) {
        CardRecommendDTO recommendDTO = chatGptService.getCardBenefit(sellerDTO, memberNo);
        System.out.println(sellerDTO.toString());
        System.out.println(memberNo);
        if(recommendDTO == null) {
            return ResponseEntity.status(500).body("추천 카드 없음");
        }
        ;
        return ResponseEntity.ok(recommendDTO);

//        try {
//            CardRecommendDTO recommendDTO = chatGptService.getCardBenefit(sellerDTO, memberNo);
//
//            return ResponseEntity.ok(recommendDTO.toString());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("추천 로직 실패");
//        }
    }

    @PostMapping("/request")
    public CompletableFuture<Integer> receivePaymentRequestAI(@RequestBody PayInfoDTO payInfoDTO) {
        return paymentService.secondSaveHistory(payInfoDTO)
                .thenApply(result -> {
                    // result에 결제 결과 메시지가 담겨 있음
                    return result; // 0: 승인 1: 정보 불일치 2: 유효기간 만료 3: 한도초과
                })
                .exceptionally(ex -> {
                    log.error("Error occurred", ex);
                    return -1;
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                });
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Object[]>> getCardRankList(@RequestParam String category){
        return ResponseEntity.ok(paymentService.cardRankList(category));
    }

    @GetMapping("/completed")
    public ResponseEntity<PayInfo> completePayment(@RequestParam String orderNo) {
        return ResponseEntity.ok(paymentService.getPayInfo(orderNo));
    }

    @PostMapping("/card")
    public ResponseEntity<Map<String, Object>> getCardInfo(@RequestBody Map<String, Object> map) {

        String cardCode = (String) map.get("cardCode");
        String memberNo = (String) map.get("memberNo");
        Map<String, Object> result = cardService.getMemCardInfo(cardCode, memberNo);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/done")
    public ResponseEntity<PayInfoDTO> purchaseLogicDone(@RequestParam String orderNo) {
        //TODO: 결제 완료 후 payInfo에서 데이터 뽑아와서 리턴하는 기능 추가 필요
        // payInfoRepository.findByOrderNo(orderNo)
        // 위 값으로 받아온 결과를 return
        return null;

    }

    @GetMapping("/history")
    public ResponseEntity<List<PayInfoDTO>> history(
        @RequestParam(required = false) String startDate
        , @RequestParam(required = false) String endDate
        , @RequestParam String memberNo
        , @RequestParam(required = false) String cardNo

        ) {
        try {
            // payDate, memberNo, cardNo를 이용하여 최근 결제 내역 조회
            List<PayInfoDTO> payInfoDTOs = paymentService.findByDateOrderByRegDate(startDate, endDate, memberNo, cardNo);
            return ResponseEntity.ok(payInfoDTOs); // 조회된 결제 내역 반환
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statics")
    public ResponseEntity<List<MyStaticDTO>> myStatic(
        @RequestParam String memberNo
    ) {
        try {
            List<MyStaticDTO> myStaticDTOs = paymentService.getPaymentDetails(memberNo);
            return ResponseEntity.ok(myStaticDTOs); // 조회된 결제 내역 반환
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

