package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> privateRoute(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ruta privada - Requiere autenticación con Auth0");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        response.put("user", jwt.getSubject());
        response.put("email", jwt.getClaimAsString("email"));
        response.put("name", jwt.getClaimAsString("name"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> userProfile(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil del usuario autenticado");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        response.put("user_id", jwt.getSubject());
        response.put("email", jwt.getClaimAsString("email"));
        response.put("name", jwt.getClaimAsString("name"));
        response.put("picture", jwt.getClaimAsString("picture"));
        return ResponseEntity.ok(response);
    }
} 