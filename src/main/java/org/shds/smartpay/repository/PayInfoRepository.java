package org.shds.smartpay.repository;

import org.shds.smartpay.dto.MyStaticDTO;
import org.shds.smartpay.entity.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PayInfoRepository extends JpaRepository<PayInfo, Long>, PayInfoRepositoryCustom {
    PayInfo findByOrderNo(String orderNo);

    // 날짜에 따른 결제 정보 조회
    List<PayInfo> findByPayDate(String date);

    // 날짜 있으면 최근 1주일, 없으면 해당 일자
    @Query("""
        select p
        from PayInfo p
        where (p.payDate between :startDate and :endDate) and p.memberNo = :memberNo
        order by p.regDate desc
    """)
    List<PayInfo> findByDateOrderByRegDate(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
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

    @Query(value = """
        WITH current_month_total AS (
            SELECT 
                SUM(price) AS total_price
            FROM 
                pay_info 
            WHERE 
                member_no = :memberNo
                AND pay_date BETWEEN DATE_FORMAT(CURDATE(), '%Y%m01') AND LAST_DAY(CURDATE())
        ),
        last_month_total AS (
            SELECT 
                SUM(price) AS total_price
            FROM 
                pay_info 
            WHERE 
                member_no = :memberNo
                AND pay_date BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y%m01') AND LAST_DAY(CURDATE() - INTERVAL 1 MONTH)
        )
        SELECT
            franchise_code as franchiseCode,
            
            -- 이번 달의 결제 총액
            SUM(CASE 
                WHEN pay_date BETWEEN DATE_FORMAT(CURDATE(), '%Y%m01') AND LAST_DAY(CURDATE()) 
                THEN price 
                ELSE 0 
            END) AS thisMonth,

            -- 지난 달의 결제 총액
            SUM(CASE 
                WHEN pay_date BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y%m01') AND LAST_DAY(CURDATE() - INTERVAL 1 MONTH) 
                THEN price 
                ELSE 0 
            END) AS lastMonth,

            -- 이번 달의 전체 총액
            (SELECT total_price FROM current_month_total) AS thisMonthTotal,

            -- 지난 달의 전체 총액
            (SELECT total_price FROM last_month_total) AS lastMonthTotal

        FROM
            pay_info
        WHERE
            member_no = :memberNo
            AND (
                pay_date BETWEEN DATE_FORMAT(CURDATE(), '%Y%m01') AND LAST_DAY(CURDATE())
                OR
                pay_date BETWEEN DATE_FORMAT(CURDATE() - INTERVAL 1 MONTH, '%Y%m01') AND LAST_DAY(CURDATE() - INTERVAL 1 MONTH)
            )
        GROUP BY
            franchise_code
        """, nativeQuery = true)
    List<Object[]> getPaymentDetails(@Param("memberNo") String memberNo);
}
