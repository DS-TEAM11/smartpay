package org.shds.smartpay.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.dto.CardRecommendDTO;
import org.shds.smartpay.dto.HistoryDTO;
import org.shds.smartpay.dto.PayInfoDTO;
import org.shds.smartpay.dto.SellerDTO;
import org.shds.smartpay.service.ChatGptService;
import org.shds.smartpay.service.PaymentService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static net.minidev.json.JSONValue.isValidJson;

@RestController
@RequestMapping("/api/payment/")
@Log4j2
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final ChatGptService chatGptService;

    @PostMapping("/ai")
    public ResponseEntity<String> receivePaymentRequest(@RequestBody SellerDTO sellerDTO, @RequestParam String memberNo) {
        CardRecommendDTO recommendDTO = chatGptService.getCardBenefit(sellerDTO, memberNo);
        if(recommendDTO == null) {
            return ResponseEntity.status(500).body("추천 카드 없음");
        }
        return ResponseEntity.ok(recommendDTO.toString());

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

    @GetMapping("/done")
    public ResponseEntity<PayInfoDTO> purchaseLogicDone(@RequestParam String orderNo) {
        //TODO: 결제 완료 후 payInfo에서 데이터 뽑아와서 리턴하는 기능 추가 필요
        // payInfoRepository.findByOrderNo(orderNo)
        // 위 값으로 받아온 결과를 return
        return null;

    }


}

