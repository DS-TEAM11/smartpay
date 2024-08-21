package org.shds.smartpay.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.shds.smartpay.entity.Order;
import org.shds.smartpay.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order getOrderByOrderId(String orderId) {
        return orderRepository.getOrderByOrderId(orderId);
    }
}
