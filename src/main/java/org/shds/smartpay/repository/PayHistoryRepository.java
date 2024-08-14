package org.shds.smartpay.repository;

import org.shds.smartpay.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<History, Long> {

}
