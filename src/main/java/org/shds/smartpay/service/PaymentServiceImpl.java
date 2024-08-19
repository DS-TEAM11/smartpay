package org.shds.smartpay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.dto.PayDTO;
import org.shds.smartpay.dto.PayInfoDTO;
import org.shds.smartpay.entity.History;
import org.shds.smartpay.repository.MemberRepository;
import org.shds.smartpay.repository.PayHistoryRepository;
import org.shds.smartpay.repository.PayInfoRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.shds.smartpay.entity.Member;

import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final PayInfoRepository payInfoRepository;
    private final WebClient companyAPI;

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
                .franchiseNo(payInfoDTO.getFranchiseNo())
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
                .franchiseNo(payInfoDTO.getFranchiseNo())
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
                .franchiseNo(payInfoDTO.getFranchiseNo())
                .requestName(memberName)
                .build();


        // 카드사에 결제 요청
        return companyAPI.post()
                .uri("/api/payment")
                .bodyValue(payDTO)//데이터 전송
                .retrieve()  // 서버로 전송해서 응답 받아옴
                .bodyToMono(Integer.class)  // 타입변환
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
                .franchiseNo(payInfoDTO.getFranchiseNo())
                .memberNo(payInfoDTO.getMemberNo())
                .approval(approval)
                .build();
        payHistoryRepository.save(history);
    }
}
