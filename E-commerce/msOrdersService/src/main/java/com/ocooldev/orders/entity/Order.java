package com.ocooldev.orders.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID; // <- Import correto

@Entity // -> Indica que essa classe é uma entidade JPA
@Table(name = "orders") // -> Define que ela será mapeada para a tabela "orders"
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;
    // -> Identificador único do pedido (UUID gerado pelo banco ou pelo JPA)

    private String customerId;
    // -> Identificador do cliente que fez o pedido

    private Double totalAmount;
    // -> Valor total do pedido

    private Instant createdAt;
    // -> Data/hora em que o pedido foi criado

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    // -> Relacionamento: um pedido pode ter vários itens

    // Getters e Setters
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

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
