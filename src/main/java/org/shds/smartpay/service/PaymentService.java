package org.shds.smartpay.service;

import org.shds.smartpay.dto.MyStaticDTO;
import org.shds.smartpay.dto.PayInfoDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.shds.smartpay.entity.PayInfo;

import java.util.List;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface PaymentService {
    String firstSaveHistory(PayInfoDTO payInfoDTO);

    CompletableFuture<Integer> secondSaveHistory(PayInfoDTO payInfoDTO);

    void thirdSaveHistory(PayInfoDTO payInfoDTO, int approval);

    void paymentcompleted(PayInfoDTO payInfoDTO);

    List<Object[]> cardRankList(@RequestParam String category);

    PayInfo getPayInfo(@RequestParam String orderNo);

    /**
     * 특정 memberNo에 해당하는 PayInfo 목록을 payDate 기준으로 내림차순으로 반환합니다.
     *
     * @param memberNo 조회할 memberNo
     * @return 해당 memberNo에 대한 PayInfo 목록
     */
    List<PayInfo> getPayInfosByMemberNo(String memberNo);

    /**
     * 회원의 카드리스트 날짜 없으면 최근 1주일 조회  or 날짜 있으면 해당 일자 조회
     * @param startDate 시작일
     * @param endDate 종료일
     * @param memberNo 회원번호
     * @param cardNo 가드번호
     * @return
     */
    List<PayInfoDTO> findByDateOrderByRegDate(String startDate, String endDate, String memberNo, String cardNo);

    List<PayInfoDTO> findByDateOrderByRegDatePage(String startDate, String endDate, String memberNo, String cardNo, int page, int size);

    Map<String, MyStaticDTO> getPaymentDetails(String memberNo);
}
