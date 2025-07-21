package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> publicRoute() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ruta pública general - Acceso libre");
        response.put("status", "success");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Map<String, Object>>> getMenu() {
        List<Map<String, Object>> menu = new ArrayList<>();

        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", "Café Americano");
        item1.put("description", "Café negro clásico, suave y aromático.");
        item1.put("price", 2.5);
        item1.put("image", "https://excelso77.com/wp-content/uploads/2024/05/por-que-el-cafe-americano-se-llama-asi-te-lo-contamos.webp");
        item1.put("available", true);
        item1.put("category", "Café");
        menu.add(item1);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("name", "Cappuccino");
        item2.put("description", "Café espresso con leche vaporizada y espuma.");
        item2.put("price", 3.0);
        item2.put("image", "https://www.livingnorth.com/images/media/articles/food-and-drink/eat-and-drink/coffee.png?fm=webp&w=1000");
        item2.put("available", true);
        item2.put("category", "Café");
        menu.add(item2);

        Map<String, Object> item3 = new HashMap<>();
        item3.put("id", 3);
        item3.put("name", "Medialuna");
        item3.put("description", "Clásica medialuna de manteca, ideal para acompañar el café.");
        item3.put("price", 1.2);
        item3.put("image", "https://www.hormelfoods.com/wp-content/uploads/culinary_collective_croissants-Recipe-2400x1000-1-2048x853.1692042257.jpg");
        item3.put("available", true);
        item3.put("category", "Pastelería");
        menu.add(item3);

        return ResponseEntity.ok(menu);
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