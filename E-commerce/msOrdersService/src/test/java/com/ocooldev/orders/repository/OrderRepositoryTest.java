package com.ocooldev.orders.repository;

import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // carrega apenas JPA + banco em memória
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveOrderWithItems() {
        Order order = new Order();
        order.setCustomerId("cust-123");
        order.setTotalAmount(200.0);

        OrderItem item = new OrderItem();
        item.setProductId("prod-456");
        item.setQuantity(2);
        item.setOrder(order);

        order.setItems(List.of(item));

        Order saved = orderRepository.save(order);

        assertNotNull(saved.getId());
        assertEquals(1, saved.getItems().size());
    }
}
