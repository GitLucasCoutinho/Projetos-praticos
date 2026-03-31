package com.ocooldev.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderItemDTO {

    @NotBlank(message = "O productId não pode ser vazio")
    private String productId;
    // -> Identificador único do produto

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
    private int quantity;
    // -> Quantidade do produto no pedido (mínimo 1)

    public OrderItemDTO() {
        // Construtor padrão necessário para a desserialização JSON
    }

    public OrderItemDTO(String productId, int quantity) {
        setProductId(productId);
        setQuantity(quantity);
    }

    // Getters e Setters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
