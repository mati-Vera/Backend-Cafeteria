package com.mati.curso.springboot.webapp.backendcafeteria.repository;

import com.mati.curso.springboot.webapp.backendcafeteria.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByAuth0Id(String auth0Id);
    Optional<Persona> findByEmail(String email);
} 