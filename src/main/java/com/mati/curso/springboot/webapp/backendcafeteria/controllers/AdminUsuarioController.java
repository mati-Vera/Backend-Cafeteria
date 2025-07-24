package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.Persona;
import com.mati.curso.springboot.webapp.backendcafeteria.repository.PersonaRepository;
import com.mati.curso.springboot.webapp.backendcafeteria.service.Auth0Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController {
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private Auth0Service auth0Service;

    // Elimino el endpoint de cambio de rol porque la sincronización con Auth0 no se usará

    @GetMapping("")
    public ResponseEntity<Iterable<Persona>> listarUsuarios() {
        return ResponseEntity.ok(personaRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Persona> buscarUsuario(@RequestParam(required = false) String email, @RequestParam(required = false) String nombre) {
        if (email != null) {
            return personaRepository.findByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else if (nombre != null) {
            // Buscar por nombre exacto (puedes mejorar esto para búsqueda parcial)
            return personaRepository.findAll().stream()
                    .filter(p -> nombre.equalsIgnoreCase(p.getNombre()))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> verUsuarioPorId(@PathVariable Long id) {
        return personaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sync-auth0")
    public ResponseEntity<String> syncAuth0Users() {
        List<java.util.Map<String, Object>> users = auth0Service.getAllAuth0Users();
        int creados = 0;
        for (var user : users) {
            String auth0Id = (String) user.get("user_id");
            String email = (String) user.get("email");
            String nombre = (String) user.getOrDefault("name", email);
            // Roles personalizados pueden estar en app_metadata o en los claims, aquí asumimos cliente por defecto
            String rol = "CLIENTE";
            if (user.containsKey("app_metadata")) {
                var appMeta = (java.util.Map<String, Object>) user.get("app_metadata");
                if (appMeta != null && appMeta.containsKey("roles")) {
                    var roles = appMeta.get("roles");
                    if (roles instanceof List && !((List<?>) roles).isEmpty()) {
                        rol = ((List<?>) roles).get(0).toString().toUpperCase();
                    }
                }
            }
            if (personaRepository.findByAuth0Id(auth0Id).isEmpty()) {
                Persona p = new Persona();
                p.setAuth0Id(auth0Id);
                p.setEmail(email);
                p.setNombre(nombre);
                p.setRol(rol);
                personaRepository.save(p);
                creados++;
            }
        }
        return ResponseEntity.ok("Usuarios sincronizados. Nuevos creados: " + creados);
    }
} 