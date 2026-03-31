package com.ocooldev.orders.events;

import com.ocooldev.orders.dto.OrderItemDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderCreatedEvent {

    private UUID orderId;
    // -> Identificador único do pedido

    private String customerId;
    // -> Identificador do cliente que fez o pedido

    private List<OrderItemDTO> items;
    // -> Lista de itens do pedido (produto + quantidade)

    private double totalAmount;
    // -> Valor total do pedido

    private Instant createdAt;
    // -> Data/hora em que o pedido foi criado

    // Getters e Setters
    public UUID getOrderId() {
        return orderId;
    }
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }
    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
