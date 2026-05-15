package com.inutri.repositorio;

import com.inutri.modelo.ResultadoAnalisis;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ResultadoAnalisisRepository extends JpaRepository<ResultadoAnalisis, Long> {
    @Query("""
        SELECT ra 
        FROM ResultadoAnalisis ra
        JOIN FETCH ra.analisis a
        JOIN FETCH ra.biomarcador b
        WHERE a.paciente.id = :pacienteId
        ORDER BY b.id, a.fecha DESC
    """)
    List<ResultadoAnalisis> findResultadosByPacienteOrdered(@Param("pacienteId") Long pacienteId);
}