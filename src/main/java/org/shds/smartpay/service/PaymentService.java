package org.shds.smartpay.service;

import org.shds.smartpay.dto.PayInfoDTO;

import java.util.concurrent.CompletableFuture;


public interface PaymentService {
    void firstSaveHistory(PayInfoDTO payInfoDTO);
    CompletableFuture<Integer> secondSaveHistory(PayInfoDTO payInfoDTO);
    void thirdSaveHistory(PayInfoDTO payInfoDTO,int approval);

}
