package com.inutri.repositorio;

import com.inutri.modelo.Patologia;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatologiaRepository extends JpaRepository<Patologia, Integer> {
    @EntityGraph(attributePaths = "criterios")
    List<Patologia> findAll();
}