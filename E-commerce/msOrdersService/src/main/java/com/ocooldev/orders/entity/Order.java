package com.ocooldev.orders.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // -> Indica que essa classe é uma entidade JPA
@Table(name = "orders") // -> Define que ela será mapeada para a tabela "orders"
public class Order {

    @Id // -> Marca o campo "id" como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // -> Define que o banco vai gerar o ID automaticamente (auto incremento)
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