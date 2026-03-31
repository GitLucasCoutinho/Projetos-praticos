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

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        OrderResponseDTO savedOrder = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}