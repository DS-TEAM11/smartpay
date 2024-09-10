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

    // memberNo로 pay_info에서 카드별 총 결제내역과 실적 1구간 조회
    @Query(
            value = """
        select x.*, y.total_price, y.card_goal1
        from card x
        left join (
            select a.card_no, sum(a.price) as total_price, a.card_code, b.card_goal1
            from pay_info a
            join card_info b
            on a.card_code = b.card_code
            where a.member_no = :memberNo
                and a.pay_date BETWEEN DATE_FORMAT(CURDATE(), '%Y%m01') AND LAST_DAY(CURDATE())
            group by a.card_no
        ) y
        on x.card_no = y.card_no
        where x.member_no = :memberNo;
        """,
            nativeQuery = true
    )
    List<Object[]> findByMemberNoGroupByPayInfoAndCardGoal1(@Param("memberNo") String memberNo);

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


    @Transactional
    @Modifying
    @Query("UPDATE Card c SET c.cardNick = :newCardNick WHERE c.cardNo = :cardNo")
    int updateCardNick(@Param("cardNo") String cardNo, @Param("newCardNick") String newCardNick);
}
