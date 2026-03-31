package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.dto.OrderItemDTO;
import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.entity.OrderItem;
import com.ocooldev.orders.events.OrderCreatedEvent;
import com.ocooldev.orders.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service // -> Indica que essa classe é um Service gerenciado pelo Spring
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
    // -> KafkaTemplate configurado para enviar objetos do tipo OrderCreatedEvent

    // -> Construtor com injeção de dependências
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // -> Converte o DTO para a entidade JPA
        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setCreatedAt(Instant.now());

        // -> Mapeia os itens do DTO para entidades
        List<OrderItem> items = dto.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(i.getProductId());
                    item.setQuantity(i.getQuantity());
                    item.setOrder(order);
                    return item;
                }).toList();
        order.setItems(items);

        // -> Salva no banco
        Order savedOrder = orderRepository.save(order);

        // -> Monta o evento conforme contrato OrderCreated-V1
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(savedOrder.getId());
        event.setCustomerId(savedOrder.getCustomerId());
        event.setItems(dto.getItems()); // usa os DTOs recebidos
        event.setTotalAmount(savedOrder.getTotalAmount());
        event.setCreatedAt(savedOrder.getCreatedAt());

        // -> Publica evento no Kafka
        kafkaTemplate.send("order-created", event);

        // -> Converte a entidade salva para DTO de resposta
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setCustomerId(savedOrder.getCustomerId()); // corrigido: agora usa customerId
        response.setTotalAmount(savedOrder.getTotalAmount());

        return response;
    }
}
