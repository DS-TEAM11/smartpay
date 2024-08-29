package org.shds.smartpay.service;

import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.dto.CardDTO;
import org.shds.smartpay.entity.BinTable;
import org.shds.smartpay.entity.Card;
import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.repository.BinTableRepository;
import org.shds.smartpay.repository.CardInfoRepository;
import org.shds.smartpay.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private BinTableRepository binTableRepository;

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private CardRepository cardRepository;


    @Override
    public Optional<BinTableDTO> getCardCompanyByBin(String cardNumber) {
        // 6자리에서 9자리까지의 BIN을 순차적으로 시도
        for (int length = 9; length >= 6; length--) {
            if (cardNumber.length() >= length) {
                String bin = cardNumber.substring(0, length);
                Optional<BinTable> binTable = binTableRepository.findByBin(bin);
                if (binTable.isPresent()) {
                    BinTable table = binTable.get();
                    BinTableDTO binTableDTO = BinTableDTO.builder()
                            .binNo(table.getBinNo())
                            .argName(table.getArgName())
                            .bin(table.getBin())
                            .isCredit(table.getIsCredit())
                            .build();
                    return Optional.of(binTableDTO);
                }
            }
        }
        // 일치하는 BIN이 없는 경우 빈 Optional 반환
        return Optional.empty();
    }

    @Override
    public List<CardInfo> getCardsByCompany(String cardCompany) {
        return cardInfoRepository.findByCardCompany(cardCompany);
    }

    @Override
    public List<Card> getCardsByMemberNo(String memberNo) {
        return cardRepository.findByMemberNo(memberNo);
    }

    //memberNo와 cardCode로 조회한 해당 카드 정보
    @Override
    public Map<String, Object> getMemCardInfo(String cardNo, String memberNo) {
        return cardInfoRepository.getMemCardInfo(cardNo, memberNo);
    }

    @Override
    public int updateByBenefitPriorityAndUsagePriority(
        Integer benefitPriority
        , Integer usagePriority
        , String memberNo
        , String cardNo
    ) {



        return cardRepository.updateByBenefitPriorityAndUsagePriority(
            benefitPriority
            , usagePriority
            , memberNo
            , cardNo
        );
    }
}
