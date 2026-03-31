package com.ocooldev.orders.repository;

// -> Importa a entidade Order, que representa a tabela "orders" no banco
import com.ocooldev.orders.entity.Order;

// -> Importa a interface JpaRepository do Spring Data JPA
//    Ela fornece métodos prontos para operações no banco (save, findAll, findById, delete, etc.)
import org.springframework.data.jpa.repository.JpaRepository;

// -> Interface que estende JpaRepository
//    O primeiro parâmetro é a entidade (Order)
//    O segundo é o tipo da chave primária (Long)
public interface OrderRepository extends JpaRepository<Order, Long> {
    // -> Não precisa escrever nada aqui por enquanto.
    //    O Spring Data JPA já gera automaticamente os métodos básicos de CRUD.
    //    Exemplo: orderRepository.save(order), orderRepository.findAll(), orderRepository.findById(id).

    // -> Se você quiser consultas personalizadas, pode adicionar métodos aqui.
    //    Exemplo: List<Order> findByCustomerName(String customerName);
}
