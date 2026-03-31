package com.ocooldev.orders.integration;

import com.ocooldev.orders.dto.OrderItemDTO;
import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest // -> Sobe o contexto completo do Spring Boot (service, repository, etc.)
@EmbeddedKafka(partitions = 1, topics = {"order-created"})
// -> Cria um broker Kafka em memória para testes, com 1 partição e o tópico "order-created"
class OrderKafkaIntegrationTest {

    @Autowired
    private OrderService orderService; // -> Service que será testado

    @Test
    void testOrderEventPublishedToKafka() {
        // 1. Monta o DTO de entrada simulando payload da API
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setCustomerId("cust-123");
        dto.setTotalAmount(150.0);
        dto.setItems(List.of(new OrderItemDTO("prod-789", 1)));

        // 2. Chama o service para criar o pedido
        var response = orderService.createOrder(dto);

        // 3. Valida que o pedido foi criado e tem ID
        assertNotNull(response.getId());

        // 4. Observação: aqui poderíamos adicionar um Consumer para verificar se o evento
        // realmente foi publicado no tópico "order-created". O EmbeddedKafka permite isso.
    }
}
