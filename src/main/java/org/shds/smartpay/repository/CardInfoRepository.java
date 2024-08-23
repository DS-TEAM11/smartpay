package org.shds.smartpay.repository;

import org.shds.smartpay.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface CardInfoRepository extends JpaRepository<CardInfo, String> {

    // cardCompany로 CardInfo 목록을 가져오는 메서드
    List<CardInfo> findByCardCompany(String cardCompany);
    Optional<CardInfo> findByCardCode(String cardCode);


    //card_code와 member_no로 카드 정보 조회
    @Query(value = "SELECT ci.card_code, ci.card_img, ci.card_company, ci.card_name, c.card_nick, substr(c.card_no, 13, 16) as lastNums " +
            "FROM card_info ci, card c " +
            "WHERE ci.card_code = c.card_code AND c.card_code = :cardCode " +
            "AND c.member_no = :memberNo", nativeQuery = true)
    Map<String, Object> getMemCardInfo(String cardCode, String memberNo);

}
