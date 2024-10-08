package org.shds.smartpay.controller;

import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.dto.CardDTO;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.repository.CardInfoRepository;
import org.shds.smartpay.repository.CardRepository;
import org.shds.smartpay.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardInfoRepository cardInfoRepository;


    // 카드 등록하는 것, 카드 비밀번호 암호화
    @PostMapping
    public ResponseEntity<Card> registerCard(@RequestBody CardDTO cardDTO) {

        // DTO에서 카드 비밀번호를 암호화
        String encodedPwd = passwordEncoder.encode(cardDTO.getCardPwd());
        cardDTO.setCardPwd(encodedPwd); // 암호화된 비밀번호를 DTO에 설정

        //CardInfo에서 CardCode 해당하는 이미지 들고오기
        Optional<CardInfo> cardInfoOptional = cardInfoRepository.findByCardCode(cardDTO.getCardCode());

        if (cardInfoOptional.isPresent()) {
            CardInfo cardInfo = cardInfoOptional.get();
            cardDTO.setCardImage(cardInfo.getCardImg());
            cardDTO.setCardName(cardInfo.getCardName());
        } else {
            System.out.println("X");
        }

        // DTO를 엔티티로 변환
        Card card = convertToEntity(cardDTO);

        // 엔티티를 DB에 저장
        Card savedCard = cardRepository.save(card);
        return ResponseEntity.ok(savedCard);
    }

    //빌더로 저장해서 카드값 넘기기
    private Card convertToEntity(CardDTO cardDTO) {
        return Card.builder()
                .cardNo(cardDTO.getCardNo())
                .cardNick(cardDTO.getCardNick())
                .cardName(cardDTO.getCardName())
                .isCredit(cardDTO.getIsCredit())
                .cardPwd(cardDTO.getCardPwd())  // 암호화된 비밀번호 사용
                .validPeriod(cardDTO.getValidPeriod())
                .regUser(cardDTO.getRegUser())
                .cardCode(cardDTO.getCardCode())
                .memberNo(cardDTO.getMemberNo())
                .cardImage(cardDTO.getCardImage())
                .build();
    }

    // bin 조회하게 하기
    @GetMapping("/company")
    public ResponseEntity<Optional> getCardCompany(@RequestParam String cardNumber) {
        // 카드 번호의 길이 확인
        if (cardNumber.length() < 6) {
            return ResponseEntity.badRequest().body(null); // 잘못된 카드 번호 형식에 대한 응답
        }

        // 카드 번호 로깅
        log.info("Received cardNumber: " + cardNumber);

        Optional<BinTableDTO> binInfo = cardService.getCardCompanyByBin(cardNumber);
        if (binInfo.isPresent()) {
            return ResponseEntity.ok(binInfo);
        } else {
            System.out.println("No company found for cardNumber: " + cardNumber); // 로깅
            return ResponseEntity.badRequest().body(null); // 카드사가 없을 경우 404 반환
        }
    }

    //카드Info 데이터 가져오기
    @GetMapping("/byCompany")
    public ResponseEntity<List<CardInfo>> getCardsByCompany(@RequestParam String cardCompany) {
        List<CardInfo> cards = cardService.getCardsByCompany(cardCompany);
        if (cards.isEmpty()) {
            return ResponseEntity.status(404).body(null); // 해당 cardCompany가 없을 경우 404 반환
        }
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/byMember")
    public ResponseEntity<List<Card>> getCardsByMemberNo(@RequestParam String memberNo) {
        List<Card> cards = cardService.getCardsByMemberNo(memberNo);
        return ResponseEntity.ok(cards); // 카드가 없을 경우 빈 리스트를 반환
    }

    //MemberNo에 있는 카드데이터를 cardInfo와 매핑시켜서 들고와야함
    @GetMapping("/details/byMember")
    public ResponseEntity<List<CardDTO>> getCardDetailsByMemberNo(@RequestParam String memberNo) {
        // memberNo로 카드 조회
        List<Card> cards = cardRepository.findByMemberNo(memberNo);

        // 각 카드에 대해 CardInfo에서 카드 이름과 카드사 정보를 가져와 새로운 DTO 객체로 설정
        List<CardDTO> cardDTOs = cards.stream()
                .map(card -> {
                    Optional<CardInfo> cardInfoOpt = cardInfoRepository.findById(card.getCardCode());
                    return cardInfoOpt.map(cardInfo -> CardDTO.builder()
                            .cardNo(card.getCardNo())
                            .cardNick(card.getCardNick())
                            .cardName(card.getCardName())
                            .cardCompany(cardInfo.getCardCompany()) // 카드 회사 추가
                            .isCredit(card.getIsCredit())
                            .cardPwd(card.getCardPwd())
                            .validPeriod(card.getValidPeriod())
                            .regUser(card.getRegUser())
                            .cardCode(card.getCardCode())
                            .memberNo(card.getMemberNo())
                            .cardImage(card.getCardImage()) // 카드 이미지 설정
                            .build()
                    ).orElse(CardDTO.builder()
                            .cardNo(card.getCardNo())
                            .cardNick(card.getCardNick())
                            .cardName("Unknown Card") // 기본값 설정
                            .isCredit(card.getIsCredit())
                            .cardPwd(card.getCardPwd())
                            .validPeriod(card.getValidPeriod())
                            .regUser(card.getRegUser())
                            .cardCode(card.getCardCode())
                            .memberNo(card.getMemberNo())
                            .cardImage(card.getCardImage()) // 기본 이미지를 사용
                            .build());
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cardDTOs);
    }

    //카드 삭제하기 구현
    @DeleteMapping("/{cardNo}")
    public ResponseEntity<String> deleteCard(@PathVariable String cardNo) {
        Optional<Card> cardOptional = cardRepository.findByCardNo(cardNo);
        if (cardOptional.isPresent()) {
            cardRepository.delete(cardOptional.get());
            return ResponseEntity.ok("카드가 성공적으로 삭제되었습니다.");
        } else {
            return ResponseEntity.status(404).body("카드를 찾을 수 없습니다.");
        }
    }

    //카드별칭 수정하기
    @PostMapping("/updateNickname")
    public ResponseEntity<String> updateCardNickname(@RequestBody Map<String, String> request) {
        String cardNo = request.get("cardNo");  // 카드 번호
        String newCardNick = request.get("newCardNick");  // 새로운 별칭

        // 기존 카드 조회
        Optional<Card> cardOptional = cardRepository.findByCardNo(cardNo);
        if (cardOptional.isPresent()) {
            Card card = cardOptional.get();

            // 기존 카드의 값을 가져오고, 변경할 필드만 수정하여 새로운 카드 객체 생성
            Card updatedCard = Card.builder()
                    .cardNo(card.getCardNo())
                    .cardNick(newCardNick)  // 새로운 별칭
                    .cardName(card.getCardName())
                    .isCredit(card.getIsCredit())
                    .cardPwd(card.getCardPwd())
                    .validPeriod(card.getValidPeriod())
                    .regUser(card.getRegUser())
                    .cardCode(card.getCardCode())
                    .memberNo(card.getMemberNo())
                    .cardImage(card.getCardImage())
                    .build();

            // 새로운 객체를 저장 (실제 업데이트)
            cardRepository.save(updatedCard);

            return ResponseEntity.ok("별칭이 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카드를 찾을 수 없습니다.");
        }
    }


    @GetMapping("/details/byMember2")
    public ResponseEntity<List<Object>> getCardDetailsByMemberNo2(@RequestParam String memberNo) {
        // memberNo로 카드 조회
        List<Card> cards = cardRepository.findByMemberNo(memberNo);
//memberNo로 조회한 카드의 정보가 없기 때문에 빌더로 값을 합치고 있었다.
        // 각 카드에 대해 CardInfo에서 카드 이름과 카드사 정보를 가져와 새로운 DTO 객체로 설정
        List<Optional<CardInfo>> cardInfo = cards.stream()
                .map(card -> {
                    return cardInfoRepository.findByCardCode(card.getCardCode());
                }).collect(Collectors.toList());
        List<Object> result = new ArrayList<>();
        result.add(cards);
        result.add(cardInfo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update/benefit")
    public ResponseEntity<String> updateByBenefitPriorityAndUsagePriority(
            @RequestBody List<CardDTO> cardDTOs) {

        int updatedCard = 0; // 선언/초기화
        updatedCard = cardService.updateByBenefitPriorityAndUsagePriority(cardDTOs);

        // 업데이트 결과에 따라 응답 반환
        if (updatedCard > 0) {
            return ResponseEntity.ok("Card updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Card not found.");
        }
    }
}
