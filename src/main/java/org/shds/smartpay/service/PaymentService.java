package org.shds.smartpay.service;

import org.shds.smartpay.dto.PayInfoDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface PaymentService {
    void firstSaveHistory(PayInfoDTO payInfoDTO);
    CompletableFuture<Integer> secondSaveHistory(PayInfoDTO payInfoDTO);
    void thirdSaveHistory(PayInfoDTO payInfoDTO,int approval);


    List<Object[]> cardRankList(@RequestParam String category);
}
