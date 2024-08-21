package org.shds.smartpay.repository;

import org.shds.smartpay.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardInfoRepository extends JpaRepository<CardInfo, String> {

    // cardCompany로 CardInfo 목록을 가져오는 메서드
    List<CardInfo> findByCardCompany(String cardCompany);




}
