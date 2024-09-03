package org.shds.smartpay.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.shds.smartpay.entity.PayInfo;
import org.shds.smartpay.entity.QPayInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PayInfoRepositoryCustomImpl implements PayInfoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    public PayInfoRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PayInfo> findPayInfoByDateAndMemberNoWithPaging(String startDate, String endDate, String memberNo, int page, int size) {
        QPayInfo payInfo = QPayInfo.payInfo;

        BooleanExpression dateCondition = payInfo.payDate.between(startDate, endDate);
        BooleanExpression memberCondition = payInfo.memberNo.eq(memberNo);

        return queryFactory
                .selectFrom(payInfo)
                .where(dateCondition.and(memberCondition))
                .orderBy(payInfo.regDate.desc())
                .offset((page - 1) * size)
                .limit(size)
                .fetch();
    }
}
