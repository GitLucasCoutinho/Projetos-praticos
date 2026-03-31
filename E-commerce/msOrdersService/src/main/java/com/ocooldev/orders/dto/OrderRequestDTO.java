package com.ocooldev.orders.dto;

// -> Importa a anotação @NotBlank, usada para validar que o campo não seja nulo nem vazio
import jakarta.validation.constraints.NotBlank;

// -> Importa a anotação @NotNull, usada para garantir que o campo não seja nulo
import jakarta.validation.constraints.NotNull;

// -> Importa a anotação @Positive, usada para validar que o número seja maior que zero
import jakarta.validation.constraints.Positive;

public class OrderRequestDTO {

    @NotBlank // -> Valida que o nome do cliente não pode ser vazio ou apenas espaços
    private String customerName;

    @NotNull   // -> Garante que o valor total não seja nulo
    @Positive  // -> Garante que o valor total seja positivo (> 0)
    private Double totalAmount;

    // -> Métodos getters e setters para acessar e modificar os atributos
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