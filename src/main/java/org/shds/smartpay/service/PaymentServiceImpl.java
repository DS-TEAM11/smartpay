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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
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
    public String firstSaveHistory(PayInfoDTO payInfoDTO) throws DataAccessException {
        // 첫번째 결제 로그 저장
        History history = History.builder()
                .orderNo(UUID.randomUUID().toString())
                .product(payInfoDTO.getProduct())
                .price(payInfoDTO.getPrice())
                .cardCode(payInfoDTO.getCardCode())
                .isAi(payInfoDTO.isGetIsAi())
                .gptState(0)  // 아직 AI 로직 돌기 전
                .payDate(payInfoDTO.getPayDate())
                .franchiseName(payInfoDTO.getFranchiseName())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .memberNo(payInfoDTO.getMemberNo())
                .build();
        payHistoryRepository.save(history);
        return history.getOrderNo();
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
                .franchiseName(payInfoDTO.getFranchiseName())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .franchiseName(payInfoDTO.getFranchiseName())
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
                .franchiseName(payInfoDTO.getFranchiseName())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .franchiseName(payInfoDTO.getFranchiseName())
                .requestName(memberName)
                .build();


        // 카드사에 결제 요청
        return companyAPI.post()
                .uri("/api/payment")
                .bodyValue(payDTO)//데이터 전송
                .retrieve()  // 서버로 전송해서 응답 받아옴
                .bodyToMono(Integer.class)  // 타입변환
                .map(paymentStatus -> {
                    thirdSaveHistory(payInfoDTO, paymentStatus);  //세번째 결제로그 저장
                    if(paymentStatus == 0) paymentcompleted(payInfoDTO); //결제 승인이면 pay_info 테이블 결제승인 건 저장
                    return paymentStatus;
                })
                .toFuture(); // CompletableFuture로 변환

        //세번째 결제 로그 저장


    }

    @Transactional
    @Override
    public void thirdSaveHistory(PayInfoDTO payInfoDTO, int approval) throws DataAccessException {

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
                .franchiseName(payInfoDTO.getFranchiseName())
                .memberNo(payInfoDTO.getMemberNo())
                .approval(approval)
                .build();
        payHistoryRepository.save(history);
    }

    //결제 승인 정보 pay_info 테이블 저장
    @Override
    public void paymentcompleted(PayInfoDTO payInfoDTO) throws DataAccessException  {
        PayInfo info = PayInfo.builder()
                .orderNo(payInfoDTO.getOrderNo())
                .product(payInfoDTO.getProduct())
                .price(payInfoDTO.getPrice())
                .cardNo(payInfoDTO.getCardNo())
                .cardCode(payInfoDTO.getCardCode())
                .payDate(payInfoDTO.getPayDate())
                .saveType(payInfoDTO.getSaveType())
                .savePrice(payInfoDTO.getSaveType())
                .franchiseName(payInfoDTO.getFranchiseName())
                .franchiseCode(payInfoDTO.getFranchiseCode())
                .isAi(payInfoDTO.isGetIsAi())
                .memberNo(payInfoDTO.getMemberNo())
                .build();
        payInfoRepository.save(info);
    }

    @Override
    public List<PayInfo> getPayInfosByMemberNo(String memberNo) {
        return payInfoRepository.findByMemberNoOrderByPayDateDesc(memberNo);
    }

    @Override
    public List<PayInfoDTO> findByDateOrderByPayDate(
            String startDate,
            String endDate,
            String memberNo,
            String cardNo
    ) {
        List<PayInfo> payInfos;

        // 카드 목록 초기화
        final List<Card> cardList = new ArrayList<>();

        // 카드번호가 없으면 memberNo로 전체 카드 리스트 조회
        if (cardNo == "" || cardNo == null) {
            cardList.addAll(cardRepository.findByMemberNo(memberNo));
        }
        // 카드번호가 있으면 해당 카드만 조회하여 리스트에 추가
        else {
            Optional<Card> cardOptional = cardRepository.findByCardNo(cardNo);
            cardOptional.ifPresent(cardList::add);
        }
        System.out.println("###########################################");
        System.out.println(cardList);
        System.out.println("###########################################");

        // 지불일자가 없으면 최근 일주일 범위로 PayInfo 조회
        if (startDate == "" || endDate == "" || startDate == null || endDate == null) {
            payInfos = payInfoRepository.findByDateOrderByPayDate(startDate, endDate, memberNo);
        }
        // 지불일자가 있는 경우 해당 날짜 범위로 PayInfo 조회
        else {
            payInfos = payInfoRepository.findByDateOrderByPayDate(startDate, endDate, memberNo);
        }

        System.out.println("###########################################");
        System.out.println(payInfos);
        System.out.println("###########################################");

        // PayInfo를 PayInfoDTO로 변환
        List<PayInfoDTO> result = payInfos.stream()
                .map(payInfo -> {
                    // 해당 결제 정보와 연관된 카드 정보 가져오기
                    final Card matchingCard = cardList.stream()
                            .filter(card -> card.getCardNo().equals(payInfo.getCardNo()))
                            .findFirst()
                            .orElse(null);

                    // 카드가 존재하지 않거나 cardNo가 일치하지 않는 경우 null 반환
                    if (matchingCard == null || !matchingCard.getCardNo().equals(payInfo.getCardNo())) {
                        return null;
                    }

                    // 카드 이미지가 있으면 설정
                    final String cardImage = matchingCard.getCardImage();

                    // PayInfoDTO 객체 생성
                    return PayInfoDTO.builder()
                            .orderNo(payInfo.getOrderNo())
                            .product(payInfo.getProduct())
                            .price(payInfo.getPrice())
                            .cardNo(payInfo.getCardNo())
                            .cardCode(payInfo.getCardCode())
                            .getIsAi(payInfo.getIsAi())
                            .payDate(payInfo.getPayDate())
                            .savePrice(payInfo.getSavePrice())
                            .saveType(payInfo.getSaveType())
                            .franchiseCode(payInfo.getFranchiseCode())
                            .franchiseName(payInfo.getFranchiseName())
                            .memberNo(payInfo.getMemberNo())
                            .cardImage(cardImage)  // 카드 이미지 설정
                            .build();
                })
                .filter(dto -> dto != null) // null을 필터링하여 제외
                .collect(Collectors.toList()); // 리스트로 수집

        return result;
    }

    //카테고리에 따른 랭크
    @Override
    public List<Object[]> cardRankList(@RequestParam String category) {
        return payHistoryRepository.getCardRankList(category);
    }

    //주문번호에 대한 결제 완료건 가져오기
    @Override
    public PayInfo getPayInfo(String orderNo) {
        return payInfoRepository.findByOrderNo(orderNo);
    }



}
