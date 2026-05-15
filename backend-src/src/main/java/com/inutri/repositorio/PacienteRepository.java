package com.inutri.repositorio;

import com.inutri.modelo.Paciente;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByIdAndUsuario_Id(Long id, Long usuarioId);
    @Query("SELECT p FROM Paciente p WHERE p.usuario.id = ?1")
    Page<Paciente> findAllByUsuarioId(Pageable pageable, Long usuarioId);
    @Query("""
        SELECT p FROM Paciente p
        WHERE p.usuario.id = :usuarioId
        AND (
            :busqueda IS NULL
            OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))
            OR LOWER(p.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))
        )
    """)
    Page<Paciente> buscarPorNombreOApellido(@Param("usuarioId") Long usuarioId, @Param("busqueda") String busqueda, Pageable pageable);
    @Query("""
        SELECT p FROM Paciente p
        LEFT JOIN Sesion s ON s.paciente = p AND s.fechaHora >= :fecha AND s.estado = PENDIENTE
        WHERE p.usuario.id = :usuarioId
        GROUP BY p
        ORDER BY
            CASE WHEN MIN(s.fechaHora) IS NULL THEN 1 ELSE 0 END,
            MIN(s.fechaHora),
            p.apellido ASC
    """)
    List<Paciente> buscarPacientesPorApellidoAscendenteYProximaSesion(@Param("usuarioId") Long usuarioId, @Param("fecha") LocalDateTime fecha);
    @Query("""
        SELECT p FROM Paciente p
        LEFT JOIN Sesion s ON s.paciente = p AND s.fechaHora >= :fecha AND s.estado = PENDIENTE
        WHERE p.usuario.id = :usuarioId
        GROUP BY p
        ORDER BY
            CASE WHEN MIN(s.fechaHora) IS NULL THEN 1 ELSE 0 END,
            MIN(s.fechaHora),
            p.apellido DESC
    """)
    List<Paciente> buscarPacientesPorApellidoDescendenteYProximaSesion(@Param("usuarioId") Long usuarioId, @Param("fecha") LocalDateTime fecha);
}