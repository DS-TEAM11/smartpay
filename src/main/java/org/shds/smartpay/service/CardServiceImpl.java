package org.shds.smartpay.service;

import org.shds.smartpay.dto.BinTableDTO;
import org.shds.smartpay.entity.BinTable;
import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.repository.BinTableRepository;
import org.shds.smartpay.repository.CardInfoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private BinTableRepository binTableRepository;

    @Autowired
    private CardInfoRespository cardInfoRespository;


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
        return cardInfoRespository.findByCardCompany(cardCompany);
    }
}
