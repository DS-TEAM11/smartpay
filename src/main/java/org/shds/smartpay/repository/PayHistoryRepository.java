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
}
