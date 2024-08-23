package org.shds.smartpay.repository;

import org.shds.smartpay.entity.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {
    PayInfo findByOrderNo(String orderNo);

    // 날짜에 따른 결제 정보 조회
    List<PayInfo> findByPayDate(String date);

    // 날짜 있으면 최근 1주일, 없으면 해당 일자
    @Query("""
        select p
        from PayInfo p
        where p.regDate between :startDate and :endDate and p.memberNo = :memberNo
        order by p.payDate desc
    """)
    List<PayInfo> findByDateOrderByPayDate(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("memberNo") String memberNo
    );


    // 최근 이용 내역 조회
    List<PayInfo> findByMemberNoOrderByPayDateDesc(String memberNo);


    /**
     * save_type에 따라 이번달 받은 혜택 조회 기능
     *
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
