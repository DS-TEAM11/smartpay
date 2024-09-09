package org.shds.smartpay.repository;

import org.shds.smartpay.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


public interface PayHistoryRepository extends JpaRepository<History, Long> {
    @Query(value = "SELECT SUM(price) as prespend, card_code " +
            "FROM pay_history " +
            "WHERE member_no = :memberNo " +
            "AND mid(pay_date, 5,2) = mid(date_add(curdate(), interval -1 MONTH), 6,2) " +
            "AND approval = 0 " +
            "GROUP BY card_code", nativeQuery = true)
    List<Map<String, Object>> findPreviousSpendingByMemberNo(String memberNo);

    @Query(value = "SELECT COUNT(p.card_code) AS cnt, ci.card_code, ci.card_name, ci.card_company, ci.card_img, ci.card_link " +
            "FROM card_info ci, pay_info p " +
            "WHERE p.card_code = ci.card_code AND (p.pay_date BETWEEN DATE_FORMAT(CURDATE(), '%Y%m01') AND LAST_DAY(CURDATE())) AND (ci.card_category = :category OR :category = '전체') " +
            "GROUP BY p.card_code " +
            "ORDER BY cnt DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Object[]> getCardRankList(String category);
}
