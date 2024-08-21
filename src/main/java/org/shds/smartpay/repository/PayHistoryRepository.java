package org.shds.smartpay.repository;

import org.shds.smartpay.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


public interface PayHistoryRepository extends JpaRepository<History, Long> {

}
