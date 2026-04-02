package com.ocooldev.orders.controller;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;
import com.ocooldev.orders.service.OrderServiceInterface;
import com.ocooldev.orders.security.AuthMetrics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceInterface orderService;
    private final AuthMetrics authMetrics;

    public OrderController(OrderServiceInterface orderService, AuthMetrics authMetrics) {
        this.orderService = orderService;
        this.authMetrics = authMetrics;
    }

    @Operation(
            summary = "Criação de pedido",
            description = "Recebe os dados de um pedido (OrderRequestDTO) e retorna o pedido criado (OrderResponseDTO)."
    )
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado - token JWT ausente ou inválido")
    @ApiResponse(responseCode = "500", description = "Erro interno ao processar o pedido")
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO dto
    ) {
        try {
            long start = System.currentTimeMillis();

            OrderResponseDTO savedOrder = orderService.createOrder(dto);

            authMetrics.incrementOrdersCreated();
            authMetrics.recordOrderProcessingTime(System.currentTimeMillis() - start);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);

        } catch (Exception e) {
            authMetrics.incrementOrdersFailed();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
