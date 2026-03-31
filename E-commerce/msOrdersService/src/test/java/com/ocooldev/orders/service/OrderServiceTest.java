package com.ocooldev.orders.service;

import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testCreateOrder() {
        Order order = new Order();
        order.setCustomerName("Lucas");
        order.setTotalAmount(100.0);

        Order saved = orderService.createOrder(order);

        assertNotNull(saved.getId());
        assertEquals("Lucas", saved.getCustomerName());
    }
}