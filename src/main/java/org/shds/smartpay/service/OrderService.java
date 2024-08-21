package org.shds.smartpay.service;

import org.shds.smartpay.entity.Order;

public interface OrderService {

    Order getOrderByOrderId(String orderId);
}
