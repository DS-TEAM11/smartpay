package org.shds.smartpay.controller;

import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.dto.CardDTO;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.repository.CardRepository;
import org.shds.smartpay.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

     @Autowired
     private PasswordEncoder passwordEncoder;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;


    // 카드 등록하는 것, 카드 비밀번호 암호화
    @PostMapping
    public ResponseEntity<Card> registerCard(@RequestBody CardDTO cardDTO) {

        // DTO에서 카드 비밀번호를 암호화
         String encodedPwd = passwordEncoder.encode(cardDTO.getCardPwd());
         cardDTO.setCardPwd(encodedPwd); // 암호화된 비밀번호를 DTO에 설정

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
                .isCredit(cardDTO.getIsCredit())
                .cardPwd(cardDTO.getCardPwd())  // 암호화된 비밀번호 사용
                .validPeriod(cardDTO.getValidPeriod())
                .regUser(cardDTO.getRegUser())
                .cardCode(cardDTO.getCardCode())
                .memberNo(cardDTO.getMemberNo())
                .build();
    }

    // bin 조회하게 하기
    @GetMapping("/company")
    public ResponseEntity<BinTableDTO> getCardCompany(@RequestParam String cardNumber) {
        Optional<BinTableDTO> binInfo = cardService.getCardCompanyByBin(cardNumber);
        if (binInfo.isPresent()) {
            return ResponseEntity.ok(binInfo.get());
        } else {
            return ResponseEntity.status(404).body(null);  // 카드사가 없을 경우 404 반환
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

}
