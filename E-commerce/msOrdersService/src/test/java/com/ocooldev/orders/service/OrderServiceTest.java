package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
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
        // -> Cria o DTO de entrada simulando o payload da API
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerName("Lucas");
        dto.setTotalAmount(100.0);

        // -> Chama o service, que retorna um DTO de saída
        OrderResponseDTO saved = orderService.createOrder(dto);

        // -> Valida que o ID foi gerado e que os dados estão corretos
        assertNotNull(saved.getId());
        assertEquals("Lucas", saved.getCustomerName());
    }
}
