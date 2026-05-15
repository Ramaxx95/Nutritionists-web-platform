package com.inutri.repositorio;

import com.inutri.modelo.Biomarcador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiomarcadorRepository extends JpaRepository<Biomarcador, Integer> {
    Optional<Biomarcador> findByNombre(String nombre);
}