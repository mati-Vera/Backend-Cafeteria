package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PublicController {

    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> publicRoute() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ruta pública general - Acceso libre");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Backend Cafeteria API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
} 