package com.ocooldev.orders.dto;

// -> Esta classe não precisa de imports de validação,
//    pois ela é usada apenas para devolver dados (saída da API).
//    Diferente do RequestDTO, aqui não validamos nada,
//    apenas estruturamos o que será retornado.

import java.util.UUID;

public class OrderResponseDTO {

    // -> ID do pedido gerado (UUID em formato String)
    private UUID id;

    // -> Identificador do cliente que fez o pedido
    //    (mantemos customerId para alinhar com o contrato e entidades)
    private String customerId;

    // -> Valor total do pedido
    private Double totalAmount;

    // -> Métodos getters e setters para acessar e modificar os atributos

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
