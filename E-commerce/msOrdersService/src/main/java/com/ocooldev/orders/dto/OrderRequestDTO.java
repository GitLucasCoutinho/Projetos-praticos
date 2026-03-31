package com.ocooldev.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public class OrderRequestDTO {

    @NotBlank(message = "O customerId não pode ser vazio")
    private String customerId;
    // -> Identificador único do cliente que fez o pedido

    @NotEmpty(message = "A lista de itens não pode ser vazia")
    private List<OrderItemDTO> items;
    // -> Lista de itens do pedido (produto + quantidade)

    @NotNull(message = "O valor total não pode ser nulo")
    @Positive(message = "O valor total deve ser maior que zero")
    private Double totalAmount;
    // -> Valor total do pedido

    // Getters e Setters
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

    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
