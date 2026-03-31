package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;   // -> DTO de entrada (payload da requisição)
import com.ocooldev.orders.dto.OrderResponseDTO;  // -> DTO de saída (payload da resposta)
import com.ocooldev.orders.entity.Order;          // -> Entidade JPA (persistência no banco)
import com.ocooldev.orders.repository.OrderRepository; // -> Repositório JPA
import org.springframework.kafka.core.KafkaTemplate;   // -> Para enviar mensagens ao Kafka
import org.springframework.stereotype.Service;

@Service // -> Marca a classe como um Service gerenciado pelo Spring
public class OrderService {

    private final OrderRepository orderRepository; // -> Acesso ao banco
    private final KafkaTemplate<String, String> kafkaTemplate; // -> Publicação no Kafka

    // -> Construtor com injeção de dependências
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // -> Método principal: cria um pedido a partir de um DTO
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // -> Converte o DTO para a entidade JPA
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setTotalAmount(dto.getTotalAmount());

        // -> Salva no banco
        Order savedOrder = orderRepository.save(order);

        // -> Publica evento no Kafka com o ID do pedido
        kafkaTemplate.send("order-created", savedOrder.getId().toString());

        // -> Converte a entidade salva para DTO de resposta
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(savedOrder.getId());
        response.setCustomerName(savedOrder.getCustomerName());
        response.setTotalAmount(savedOrder.getTotalAmount());

        // -> Retorna o DTO de saída para o Controller
        return response;
    }
}