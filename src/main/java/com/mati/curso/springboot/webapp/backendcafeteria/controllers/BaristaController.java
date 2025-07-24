package com.mati.curso.springboot.webapp.backendcafeteria.controllers;

import com.mati.curso.springboot.webapp.backendcafeteria.dto.CreateBaristaRequestDTO;
import com.mati.curso.springboot.webapp.backendcafeteria.dto.UserBaristaDTO;
import com.mati.curso.springboot.webapp.backendcafeteria.entity.Persona;
import com.mati.curso.springboot.webapp.backendcafeteria.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private/users")
public class BaristaController {
    private final PersonaRepository personaRepository;

    @Autowired
    public BaristaController(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserBaristaDTO>> getBaristas(@RequestParam(required = false) String role) {
        List<Persona> personas;
        if (role != null && role.equalsIgnoreCase("barista")) {
            personas = personaRepository.findAll().stream()
                .filter(p -> p.getRol() != null && p.getRol().equalsIgnoreCase("barista"))
                .collect(Collectors.toList());
        } else {
            personas = personaRepository.findAll();
        }
        List<UserBaristaDTO> dtos = personas.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBarista(@RequestBody CreateBaristaRequestDTO request) {
        // Validar que el email no exista
        if (personaRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }
        // Crear nueva Persona con rol BARISTA
        Persona persona = new Persona();
        persona.setEmail(request.getEmail());
        persona.setNombre(request.getEmail()); // O puedes pedir el nombre en el DTO
        persona.setRol("BARISTA");
        // auth0Id y password: en un sistema real, deberías crear el usuario en Auth0 y guardar el sub. Aquí lo dejamos vacío o con el email.
        persona.setAuth0Id(request.getEmail());
        Persona saved = personaRepository.save(persona);
        UserBaristaDTO dto = mapToDTO(saved);
        return ResponseEntity.ok(dto);
    }

    private UserBaristaDTO mapToDTO(Persona persona) {
        return new UserBaristaDTO(
            persona.getId(),
            persona.getEmail(),
            persona.getNombre(),
            List.of(persona.getRol())
        );
    }
} 