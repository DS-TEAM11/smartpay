package org.shds.smartpay.repository;

import org.shds.smartpay.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId")
    Order getOrderByOrderId(@Param("orderId") String orderId);
}
