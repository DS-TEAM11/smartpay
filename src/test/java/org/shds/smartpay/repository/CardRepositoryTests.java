package org.shds.smartpay.repository;

import org.junit.jupiter.api.Test;
import org.shds.smartpay.entity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class CardRepositoryTests {

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void updateByBenefitPriorityAndUsagePriority() {
        int result = cardRepository.updateByBenefitPriorityAndUsagePriority(
                1,
                1,
                "test",
                "4006966666666666"
        );

        System.out.println(result);

        Optional<Card> updatedCard = cardRepository.findByCardNo("4006966666666666");

        System.out.println("#################################");
        System.out.println(updatedCard);
        System.out.println("#################################");
    }
}
