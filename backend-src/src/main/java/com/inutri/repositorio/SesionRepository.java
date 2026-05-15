package com.inutri.repositorio;

import com.inutri.modelo.Sesion;
import com.inutri.modelo.enums.EstadoSesion;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.*;

public interface SesionRepository extends JpaRepository<Sesion, Long> {
    Page<Sesion> findByPaciente_IdOrderByFechaHoraDesc(Long pacienteId, Pageable pageable);
    Page<Sesion> findByPaciente_Usuario_IdOrderByFechaHoraDesc(Long usuarioId, Pageable pageable);
    Optional<Sesion> findByIdAndPaciente_Id(Long sesionId, Long pacienteId);
    @Query("""
        SELECT s FROM Sesion s
        WHERE s.paciente.usuario.id = :usuarioId
        AND (:fechaInicio IS NULL OR s.fechaHora >= :fechaInicio)
        AND (:fechaFin IS NULL OR s.fechaHora <= :fechaFin)
    """)
    Page<Sesion> findByUsuarioAndFecha(@Param("usuarioId") Long usuarioId, @Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin, Pageable pageable);
    @Query("""
        SELECT FUNCTION('DATE_FORMAT', s.fechaHora, '%Y-%m') AS mes, s.estado, COUNT(s) AS cantidad
        FROM Sesion s
        WHERE s.paciente.id = :pacienteId
        AND (:fechaInicio IS NULL OR s.fechaHora >= :fechaInicio)
        AND (:fechaFin IS NULL OR s.fechaHora <= :fechaFin)
        GROUP BY mes, s.estado
        ORDER BY mes
    """)
    List<Object[]> contarSesionesPorMesYEstadoFiltrandoPorFechas(@Param("pacienteId") Long pacienteId,@Param("fechaInicio") LocalDateTime fechaInicio,@Param("fechaFin") LocalDateTime fechaFin);
    @Query("""
        SELECT FUNCTION('DATE_FORMAT', s.fechaHora, '%Y-%m') AS mes, s.estado, COUNT(s)
        FROM Sesion s
        WHERE s.paciente.usuario.id = :usuarioId
        AND (:desde IS NULL OR s.fechaHora >= :desde)
        AND (:hasta IS NULL OR s.fechaHora <= :hasta)
        GROUP BY mes, s.estado
        ORDER BY mes
    """)
    List<Object[]> contarSesionesMensualesPorEstadoDeUsuario(@Param("usuarioId") Long usuarioId,@Param("desde") LocalDateTime desde,@Param("hasta") LocalDateTime hasta);
    @Query("""
        SELECT FUNCTION('DAYOFWEEK', s.fechaHora) AS diaSemana, s.estado, COUNT(s)
        FROM Sesion s
        WHERE s.paciente.usuario.id = :usuarioId
        AND (:desde IS NULL OR s.fechaHora >= :desde)
        AND (:hasta IS NULL OR s.fechaHora <= :hasta)
        GROUP BY diaSemana, s.estado
        ORDER BY diaSemana
    """)
    List<Object[]> contarSesionesSemanalesPorEstadoDeUsuario(@Param("usuarioId") Long usuarioId, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
    @Query("""
        SELECT FUNCTION('YEAR', s.fechaHora) AS anio, s.estado, COUNT(s)
        FROM Sesion s
        WHERE s.paciente.usuario.id = :usuarioId
        AND (:desde IS NULL OR s.fechaHora >= :desde)
        AND (:hasta IS NULL OR s.fechaHora <= :hasta)
        GROUP BY anio, s.estado
        ORDER BY anio
    """)
    List<Object[]> contarSesionesAnualesPorEstadoDeUsuario(@Param("usuarioId") Long usuarioId, @Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);
    Optional<Sesion> findTopByPaciente_IdAndFechaHoraBeforeAndEstadoOrderByFechaHoraDesc(Long pacienteId, LocalDateTime fecha, EstadoSesion estado);
    Optional<Sesion> findTopByPaciente_IdAndFechaHoraAfterAndEstadoOrderByFechaHoraAsc(Long pacienteId, LocalDateTime fecha, EstadoSesion estado);
    Optional<Sesion> findTopByPaciente_IdAndFechaHoraLessThanEqualOrderByFechaHoraDesc(Long pacienteId, LocalDateTime fechaHora);
}