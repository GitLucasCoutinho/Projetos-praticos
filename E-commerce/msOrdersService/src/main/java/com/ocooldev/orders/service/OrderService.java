package com.ocooldev.orders.service;

// Importa a entidade Order, que representa o pedido no banco
import com.ocooldev.orders.entity.Order;
// Importa o repositório JPA para salvar e buscar pedidos
import com.ocooldev.orders.repository.OrderRepository;
// Importa o KafkaTemplate, usado para enviar mensagens para o Kafka
import org.springframework.kafka.core.KafkaTemplate;
// Marca a classe como um componente de serviço do Spring
import org.springframework.stereotype.Service;

@Service // -> Indica que essa classe é um "Service" gerenciado pelo Spring (injeção de dependência)
public class OrderService {

    // -> Dependência para acessar o banco de dados (Postgres)
    private final OrderRepository orderRepository;
    // -> Dependência para enviar mensagens para o Kafka
    private final KafkaTemplate<String, String> kafkaTemplate;

    // -> Construtor: o Spring injeta automaticamente o OrderRepository e o KafkaTemplate
    public OrderService(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // -> Método principal: cria um pedido
    public Order createOrder(Order order) {
        // -> Salva o pedido no banco de dados usando o JPA Repository
        Order savedOrder = orderRepository.save(order);

        // -> Publica uma mensagem no Kafka, no tópico "order-created"
        //    O conteúdo da mensagem é o ID do pedido salvo, convertido para String
        kafkaTemplate.send("order-created", savedOrder.getId().toString());

        // -> Retorna o pedido salvo para o Controller
        return savedOrder;
    }
}
