package com.example.spring.boot.sharding.jpa.repository;


import com.example.spring.boot.sharding.jpa.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
