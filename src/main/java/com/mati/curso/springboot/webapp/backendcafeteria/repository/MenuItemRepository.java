package com.mati.curso.springboot.webapp.backendcafeteria.repository;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    // Métodos personalizados si se necesitan
} 