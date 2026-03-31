package com.ocooldev.orders.entity;

import jakarta.persistence.*;

@Entity // -> Indica que essa classe será mapeada como tabela no banco
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // -> Identificador único do item (chave primária)

    private String productId;
    // -> Identificador do produto (vem do DTO)

    private int quantity;
    // -> Quantidade do produto no pedido

    @ManyToOne // -> Muitos itens pertencem a um único pedido
    @JoinColumn(name = "order_id")
    private Order order;
    // -> Relacionamento com a entidade Order

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public Order getOrder() {
        return order;
    }
    public void setOrder(Order order) {
        this.order = order;
    }
}
