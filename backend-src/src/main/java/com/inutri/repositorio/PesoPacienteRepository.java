package com.inutri.repositorio;

import com.inutri.modelo.PesoPaciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface PesoPacienteRepository extends JpaRepository<PesoPaciente, Long> {
    List<PesoPaciente> findByPacienteIdOrderByFechaAsc(Long pacienteId);
    Optional<PesoPaciente> findByPacienteIdAndFecha(Long pacienteId, LocalDate fecha);
    Optional<PesoPaciente> findBySesion_Id(Long sesionId);
}