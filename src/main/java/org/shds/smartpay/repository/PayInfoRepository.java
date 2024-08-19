package org.shds.smartpay.repository;

import org.shds.smartpay.entity.PayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayInfoRepository extends JpaRepository<PayInfo, Long> {

}
