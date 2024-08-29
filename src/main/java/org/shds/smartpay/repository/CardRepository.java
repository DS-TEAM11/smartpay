package org.shds.smartpay.repository;

import org.shds.smartpay.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {

    // 카드번호로 카드 조회
    Optional<Card> findByCardNo(String cardNo);
    List<Card> findByMemberNo(String memberNo);

    @Transactional
    @Modifying // update
    @Query("""
        update Card c
        set c.benefitPriority = :benefitPriority, c.usagePriority = :usagePriority
        where c.memberNo = :memberNo and c.cardNo = :cardNo
    """)
    public int updateByBenefitPriorityAndUsagePriority(
        @Param("benefitPriority") Integer benefitPriority,
        @Param("usagePriority") Integer usagePriority,
        @Param("memberNo") String memberNo,
        @Param("cardNo") String cardNo
    );
}
