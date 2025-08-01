package com.mati.curso.springboot.webapp.backendcafeteria.repository;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
} 