package org.shds.smartpay.repository;

import org.shds.smartpay.entity.PayInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayInfoRepository extends JpaRepository<PayInfo, Long>
        , QuerydslPredicateExecutor<PayInfo>
        , PayInfoRepositoryCustom {

    // 날짜별, 카드별 조회
    List<PayInfo> findByPayDateAndCardNo(Long from, Long to, Pageable pageable);

    // 최근 이용 내역 조회
    List<PayInfo> findByMemberNoOrderByPayDateDesc(String memberNo);

//    @Query("""
//
//
//    """)
//    List<PayInfo> findByMno

    /**
     * save_type에 따라 이번달 받은 혜택 조회 기능
     * @param saveType 0
     * @return
     */
    @Query("""
        select sum(p.savePrice)
        from PayInfo p
        where p.payDate between function('date_format', current_date , '%Y%m01') 
        and function('date_format', current_date, '%Y%m%d')
        and p.saveType = :saveType 
   """)
    Long sumSavePriceForCurrentMonth(@Param("saveType") int saveType);
}
