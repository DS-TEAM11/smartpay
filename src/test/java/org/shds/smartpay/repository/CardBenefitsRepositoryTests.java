package org.shds.smartpay.repository;

import groovy.util.logging.Log4j2;
import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.CardBenefits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Log4j2
@SpringBootTest
public class CardBenefitsRepositoryTests {
    private static final Logger log = LoggerFactory.getLogger(CardBenefitsRepositoryTests.class);
    @Autowired
    private CardBenefitsRepository repository;

    @Test
    public void getInformation(){
        String cardCode = "03040027";
        List<CardBenefits> answer = repository.findAllByCardCode(cardCode);
        log.info(answer.toString());
    }
}
