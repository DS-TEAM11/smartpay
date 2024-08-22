package org.shds.smartpay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.dto.PayDTO;
import org.shds.smartpay.dto.PayInfoDTO;
import org.shds.smartpay.entity.*;
import org.shds.smartpay.repository.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final PayInfoRepository payInfoRepository;
    private final WebClient companyAPI;
    private final CardRepository cardRepository;

    @Transactional
    @Override
    public void firstSaveHistory(PayInfoDTO payInfoDTO) throws DataAccessException {
        // 첫번째 결제 로그 저장
        History history = History.builder()
                .orderNo(payInfoDTO.getOrderNo())
                .product(payInfoDTO.getProduct())
                .price(payInfoDTO.getPrice())
                .cardCode(payInfoDTO.getCardCode())
                .isAi(payInfoDTO.isGetIsAi())
                .gptState(0)  // 아직 AI 로직 돌기 전
                .payDate(payInfoDTO.getPayDate())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .memberNo(payInfoDTO.getMemberNo())
                .build();
        payHistoryRepository.save(history);
    }

    @Transactional
    @Override
    public CompletableFuture<Integer> secondSaveHistory(PayInfoDTO payInfoDTO) throws DataAccessException {
        // 두번째 결제 로그 저장
        History history = History.builder()
                .orderNo(payInfoDTO.getOrderNo())
                .product(payInfoDTO.getProduct())
                .price(payInfoDTO.getPrice())
                .cardNo(payInfoDTO.getCardNo())
                .cardCode(payInfoDTO.getCardCode())
                .isAi(payInfoDTO.isGetIsAi())
                .gptState(1)  // AI 로직 돌고난 후
                .payDate(payInfoDTO.getPayDate())
                .saveType(payInfoDTO.getSaveType())
                .savePrice(payInfoDTO.getSaveType())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .memberNo(payInfoDTO.getMemberNo())
                .build();
        payHistoryRepository.save(history);

        //해당 멤버 이름 가져와서 보내야 함
        String memberName = memberRepository.findById(payInfoDTO.getMemberNo())
                .map(Member::getName)
                .orElse(null);

        log.info(memberName);

        //카드사에 보낼 정보
        PayDTO payDTO = PayDTO.builder()
                .cardNo(payInfoDTO.getCardNo())
                .price(payInfoDTO.getPrice())
                .product(payInfoDTO.getProduct())
                .payDate(payInfoDTO.getPayDate())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .requestName(memberName)
                .build();


        // 카드사에 결제 요청
        return companyAPI.post()
                .uri("/api/payment")
                .bodyValue(payDTO)//데이터 전송
                .retrieve()  // 서버로 전송해서 응답 받아옴
                .bodyToMono(Integer.class)  // 타입변환
                .publishOn(Schedulers.boundedElastic())  // 타입변환
                .map(paymentStatus -> {
                    thirdSaveHistory(payInfoDTO, paymentStatus);
                    return paymentStatus;
                })
                .toFuture(); // CompletableFuture로 변환

        //세번째 결제 로그 저장


    }

    @Transactional
    @Override
    public void thirdSaveHistory(PayInfoDTO payInfoDTO, int approval) {

        // 세번째 결제 로그 저장
        History history = History.builder()
                .orderNo(payInfoDTO.getOrderNo())
                .product(payInfoDTO.getProduct())
                .price(payInfoDTO.getPrice())
                .cardNo(payInfoDTO.getCardNo())
                .cardCode(payInfoDTO.getCardCode())
                .isAi(payInfoDTO.isGetIsAi())
                .gptState(1)  // AI 로직 돌고난 후
                .payDate(payInfoDTO.getPayDate())
                .saveType(payInfoDTO.getSaveType())
                .savePrice(payInfoDTO.getSaveType())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .memberNo(payInfoDTO.getMemberNo())
                .approval(approval)
                .build();
        payHistoryRepository.save(history);
    }

    @Override
    public List<PayInfo> getPayInfosByMemberNo(String memberNo) {
        return payInfoRepository.findByMemberNoOrderByPayDateDesc(memberNo);
    }

    @Override
//    public List<PayInfoDTO> findByDateOrderByPayDate(String payDate, String memberNo) {
    public List<PayInfo> findByDateOrderByPayDate(String payDate, String memberNo) {
        LocalDate now = LocalDate.now();
        LocalDate startLocalDate = now.minusMonths(1);
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        List<Card> cardList = cardRepository.findByMemberNo(memberNo);

        List<PayInfo> payInfos;

        if (payDate != null) {
            // DateTimeFormatter 정의 (String 포맷에 맞춰 정의)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            // String 값을 LocalDateTime으로 변환 (기본 시간 추가)
            startDateTime = LocalDateTime.parse(payDate + "000000", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            endDateTime = LocalDateTime.parse(payDate + "235959", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            payInfos = payInfoRepository.findByDateOrderByPayDate(startDateTime, endDateTime);
        } else {
            // 최근 1주일
            startDateTime = startLocalDate.atStartOfDay();  // 시작 날짜의 시작 시간 (00:00:00)
            endDateTime = now.atTime(LocalTime.MAX);

            payInfos = payInfoRepository.findByDateOrderByPayDate(startDateTime, endDateTime);

        }

//        // PayInfo 엔티티를 PayInfoDTO로 변환
//        List<PayInfoDTO> result = payInfos.stream().map(payInfo -> {
//            // 해당 결제 정보와 연관된 카드 정보 가져오기
//            Card card = cardList.stream()
//                    .filter(c -> c.getCardNo().equals(payInfo.getCardNo()))
//                    .findFirst()
//                    .orElse(null);
//            // 카드 이미지 세팅 및 PayInfoDTO 객체 생성
//            return PayInfoDTO.builder()
//                    .orderNo(payInfo.getOrderNo())
//                    .product(payInfo.getProduct())
//                    .price(payInfo.getPrice())
//                    .cardNo(payInfo.getCardNo())
//                    .cardCode(payInfo.getCardCode())
//                    .getIsAi(payInfo.getIsAi())
//                    .payDate(payInfo.getPayDate())
//                    .savePrice(payInfo.getSavePrice())
//                    .saveType(payInfo.getSaveType())
//                    .franchiseCode(payInfo.getFranchiseCode())
//                    .memberNo(payInfo.getMemberNo())
//                    .cardImage(card != null ? card.getCardImage() : null)  // 카드 이미지 설정
//                    .build();
//        }).collect(Collectors.toList());
//
//        return result;


        return payInfos;
    }
}
