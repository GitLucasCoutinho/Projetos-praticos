package com.ocooldev.orders.dto;

// -> Esta classe não precisa de imports de validação,
//    pois ela é usada apenas para devolver dados (saída da API).
//    Diferente do RequestDTO, aqui não validamos nada,
//    apenas estruturamos o que será retornado.

public class OrderResponseDTO {

    // -> ID do pedido gerado pelo banco (chave primária)
    private Long id;

    // -> Nome do cliente que fez o pedido
    private String customerName;

    // -> Valor total do pedido
    private Double totalAmount;

    // -> Métodos getters e setters para acessar e modificar os atributos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
