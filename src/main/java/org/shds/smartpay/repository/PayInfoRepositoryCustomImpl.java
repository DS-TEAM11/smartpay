//package org.shds.smartpay.repository;
//
//import com.querydsl.jpa.impl.JPAQuery;
//import org.shds.smartpay.entity.QPayInfo;
//import org.shds.smartpay.entity.QCardInfo;
//import org.shds.smartpay.entity.PayInfo;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.util.List;
//
//public class PayInfoRepositoryCustomImpl implements PayInfoRepositoryCustom {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<PayInfo> findByPayDateAndCardNo(String payDate, String cardNo) {
//        QPayInfo payInfo = QPayInfo.payInfo;
//        QCardInfo cardInfo = QCardInfo.cardInfo;
//
//        JPAQuery<PayInfo> query = new JPAQuery<>(entityManager);
//        return query.select(payInfo)
//                .from(payInfo, cardInfo)  // 두 테이블을 조인하지 않고 나란히 명시
//                .where(
//                        payInfo.cardCode.eq(cardInfo.cardCode),  // cardCode를 기준으로 매핑
//                        payInfo.payDate.like(payDate),
//                        payInfo.cardNo.like(cardNo)
//                )
//                .orderBy(payInfo.payDate.desc())
//                .fetch();
//    }
//}
