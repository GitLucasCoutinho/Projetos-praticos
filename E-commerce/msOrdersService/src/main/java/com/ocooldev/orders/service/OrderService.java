package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.repository.OrderRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service // -> Indica que essa classe é um Service gerenciado pelo Spring
public class OrderService implements OrderServiceInterface {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    // -> Construtor com injeção de dependências
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // -> Converte o DTO para a entidade JPA
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setTotalAmount(dto.getTotalAmount());

        // -> Salva no banco
        Order savedOrder = orderRepository.save(order);

        // -> Publica evento no Kafka
        kafkaTemplate.send("order-created", savedOrder.getId().toString());

        // -> Converte a entidade salva para DTO de resposta
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setCustomerName(savedOrder.getCustomerName());
        response.setTotalAmount(savedOrder.getTotalAmount());

        return response;
    }
}
