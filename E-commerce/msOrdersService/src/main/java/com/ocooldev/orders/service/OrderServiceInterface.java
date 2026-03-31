package com.ocooldev.orders.service;

import com.ocooldev.orders.dto.OrderRequestDTO;
import com.ocooldev.orders.dto.OrderResponseDTO;

// -> Define o contrato que qualquer implementação de serviço de pedidos deve seguir
public interface OrderServiceInterface {
    OrderResponseDTO createOrder(OrderRequestDTO dto);
}
