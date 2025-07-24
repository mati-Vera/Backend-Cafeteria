package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.Order;
import com.mati.curso.springboot.webapp.backendcafeteria.service.OrderService;
import com.mati.curso.springboot.webapp.backendcafeteria.dto.OrderDTO;
import com.mati.curso.springboot.webapp.backendcafeteria.dto.OrderItemDTO;
import com.mati.curso.springboot.webapp.backendcafeteria.repository.PersonaRepository;
import com.mati.curso.springboot.webapp.backendcafeteria.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private/orders")
public class OrderController {
    private final OrderService orderService;
    private final PersonaRepository personaRepository;

    @Autowired
    public OrderController(OrderService orderService, PersonaRepository personaRepository) {
        this.orderService = orderService;
        this.personaRepository = personaRepository;
    }

    // POST /private/orders (CLIENT)
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody Order order, @AuthenticationPrincipal Jwt jwt) {
        order.setUserId(jwt.getSubject());
        order.setStatus("PENDIENTE");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order created = orderService.save(order);
        OrderDTO dto = mapOrderToDTO(created);
        return ResponseEntity.ok(dto);
    }

    // GET /private/orders (BARISTA o ADMIN)
    @PreAuthorize("hasAnyRole('BARISTA','ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.findAll();
        List<OrderDTO> dtos = orders.stream().map(this::mapOrderToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /private/orders/my (CLIENT)
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<OrderDTO>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Order> orders = orderService.findByUserId(userId);
        List<OrderDTO> dtos = orders.stream().map(this::mapOrderToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // PUT /private/orders/{id}/status (BARISTA o ADMIN)
    @PreAuthorize("hasAnyRole('BARISTA','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdate) {
        Optional<Order> orderOpt = orderService.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(statusUpdate.getStatus());
            order.setUpdatedAt(LocalDateTime.now());
            Order updated = orderService.save(order);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO para recibir el status
    public static class StatusUpdateRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // --- Métodos de mapeo ---
    private OrderDTO mapOrderToDTO(Order order) {
        String customerEmail = personaRepository.findByAuth0Id(order.getUserId())
            .map(Persona::getEmail).orElse(null);
        return new OrderDTO(
            order.getId(),
            order.getStatus(),
            order.getTotal(),
            order.getCreatedAt(),
            order.getUpdatedAt(),
            customerEmail,
            order.getItems() != null ? order.getItems().stream().map(this::mapOrderItemToDTO).collect(Collectors.toList()) : null
        );
    }

    private OrderItemDTO mapOrderItemToDTO(com.mati.curso.springboot.webapp.backendcafeteria.entity.OrderItem item) {
        String menuItemName = item.getMenuItem() != null ? item.getMenuItem().getName() : null;
        return new OrderItemDTO(
            item.getId(),
            item.getQuantity(),
            item.getPrice(),
            menuItemName
        );
    }
} 