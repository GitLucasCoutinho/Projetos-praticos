package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.dto.OrderItemDTO;
import com.ocooldev.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        dto.setCustomerId("cust-123"); // -> agora usamos customerId
        dto.setTotalAmount(100.0);

        // -> Adiciona itens ao pedido
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId("prod-456");
        item.setQuantity(2);
        dto.setItems(List.of(item));

        // -> Chama o service, que retorna um DTO de saída
        OrderResponseDTO saved = orderService.createOrder(dto);

        // -> Valida que o ID foi gerado e que os dados estão corretos
        assertNotNull(saved.getId());
        assertEquals("cust-123", saved.getCustomerId());
        assertEquals(100.0, saved.getTotalAmount());
    }
}
