package com.example.spring.boot.sharding.jpa.repository;

import com.example.spring.boot.sharding.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
