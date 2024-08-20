package org.shds.smartpay.service;

import org.shds.smartpay.entity.CardInfo;
import org.shds.smartpay.repository.CardInfoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardInfoService {

    @Autowired
    private CardInfoRespository cardInfoRespository;

    public List<CardInfo> getAllCards() {
        return cardInfoRespository.findAll();
    }

}
