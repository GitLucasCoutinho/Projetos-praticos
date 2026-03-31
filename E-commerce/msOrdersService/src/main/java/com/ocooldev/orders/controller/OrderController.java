package com.ocooldev.orders.controller;

import com.ocooldev.orders.dto.OrderRequestDTO;   // -> DTO de entrada (dados da requisição)
import com.ocooldev.orders.dto.OrderResponseDTO;  // -> DTO de saída (dados da resposta)
import com.ocooldev.orders.service.OrderServiceInterface; // -> Interface do serviço (abstração)
import com.ocooldev.orders.security.AuthMetrics; // -> Classe de métricas personalizadas
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // -> Indica que essa classe é um Controller REST (retorna JSON)
@RequestMapping("/orders") // -> Define o endpoint base: todas as rotas começam com /orders
public class OrderController {

    private final OrderServiceInterface orderService;
    private final AuthMetrics authMetrics; // -> Para registrar métricas de pedidos

    // -> Construtor: o Spring injeta automaticamente a implementação de OrderServiceInterface e AuthMetrics
    public OrderController(OrderServiceInterface orderService, AuthMetrics authMetrics) {
        this.orderService = orderService;
        this.authMetrics = authMetrics;
    }

    @PostMapping // -> Mapeia requisições HTTP POST para /orders
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO dto // -> Recebe o corpo da requisição como DTO e valida os campos
    ) {
        try {
            long start = System.currentTimeMillis();

            // -> Chama o service para criar o pedido
            OrderResponseDTO savedOrder = orderService.createOrder(dto);

            // -> Registra métrica de pedido criado
            authMetrics.incrementOrdersCreated();

            // -> Registra tempo de processamento do pedido
            authMetrics.recordOrderProcessingTime(System.currentTimeMillis() - start);

            // -> Retorna o DTO de saída com status 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);

        } catch (Exception e) {
            // -> Registra falha na criação do pedido
            authMetrics.incrementOrdersFailed();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
