package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderItemDTO;
import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.events.OrderCreatedEvent;
import com.ocooldev.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrderPublishesEvent() {
        // DTO de entrada simulando payload da API
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerId("cust-123");
        dto.setTotalAmount(100.0);

        // Cria item usando setters (não há construtor com args)
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId("prod-456");
        item.setQuantity(2);
        dto.setItems(List.of(item));

        // Mock do repositório com UUID válido
        Order fakeOrder = new Order();
        fakeOrder.setId(UUID.randomUUID()); // gera UUID válido
        fakeOrder.setCustomerId("cust-123");
        fakeOrder.setTotalAmount(100.0);

        when(orderRepository.save(any(Order.class))).thenReturn(fakeOrder);

        // Executa o service
        OrderResponseDTO response = orderService.createOrder(dto);

        // Valida saída
        assertNotNull(response.getId(), "O ID do pedido não deve ser nulo");
        assertEquals("cust-123", response.getCustomerId());
        assertEquals(100.0, response.getTotalAmount());

        // Verifica publicação no Kafka
        verify(kafkaTemplate).send(eq("order-created"), any(OrderCreatedEvent.class));
    }
}
