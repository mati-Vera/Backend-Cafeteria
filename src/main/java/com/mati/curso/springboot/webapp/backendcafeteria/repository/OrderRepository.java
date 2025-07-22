package com.mati.curso.springboot.webapp.backendcafeteria.repository;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
} 