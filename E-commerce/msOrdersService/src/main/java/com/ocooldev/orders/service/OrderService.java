package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.dto.OrderItemDTO;
import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.entity.OrderItem;
import com.ocooldev.orders.events.OrderCreatedEvent;
import com.ocooldev.orders.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service // -> Indica que essa classe é um Service gerenciado pelo Spring
public class OrderService implements OrderServiceInterface {
private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        log.info("Iniciando criação de pedido para customerId={}", dto.getCustomerId());

        Order order = new Order();
        order.setCustomerId(dto.getCustomerId());
        order.setTotalAmount(dto.getTotalAmount());
        order.setCreatedAt(Instant.now());

        List<OrderItem> items = dto.getItems().stream()
                .map(i -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(i.getProductId());
                    item.setQuantity(i.getQuantity());
                    item.setOrder(order);
                    return item;
                }).toList();
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);
        log.info("Pedido {} salvo no banco com totalAmount={}", savedOrder.getId(), savedOrder.getTotalAmount());

        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(savedOrder.getId());
        event.setCustomerId(savedOrder.getCustomerId());
        event.setItems(dto.getItems());
        event.setTotalAmount(savedOrder.getTotalAmount());
        event.setCreatedAt(savedOrder.getCreatedAt());

        kafkaTemplate.send("order-created", event);
        log.info("Evento OrderCreated publicado no Kafka para orderId={}", savedOrder.getId());

        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setCustomerId(savedOrder.getCustomerId());
        response.setTotalAmount(savedOrder.getTotalAmount());

        log.info("Pedido {} finalizado e retornado ao cliente", savedOrder.getId());
        return response;
    }
}
