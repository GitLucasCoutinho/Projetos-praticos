package com.ocooldev.orders.controller;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.entity.Order;
import com.ocooldev.orders.service.OrderService;
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

    private final OrderService orderService; // -> Dependência para acessar a lógica de negócio

    // -> Construtor: o Spring injeta automaticamente o OrderService
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping // -> Mapeia requisições HTTP POST para /orders
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto ) { // -> Recebe o corpo da requisição como DTO e valida os campos

        // -> Chama o service para criar o pedido
        OrderResponseDTO savedOrder = orderService.createOrder(dto);

        // -> Retorna o DTO de saída com status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}