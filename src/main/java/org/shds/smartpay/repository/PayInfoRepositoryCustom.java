package org.shds.smartpay.repository;

import org.shds.smartpay.entity.PayInfo;

import java.util.List;

public interface PayInfoRepositoryCustom {
    List<PayInfo> findPayInfoByDateAndMemberNoWithPaging(String startDate, String endDate, String memberNo, int page, int size);
}
