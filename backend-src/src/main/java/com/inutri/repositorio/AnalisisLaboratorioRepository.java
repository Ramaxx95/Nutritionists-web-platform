package com.inutri.repositorio;

import com.inutri.modelo.AnalisisLaboratorio;
import com.inutri.modelo.Paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

public interface AnalisisLaboratorioRepository extends JpaRepository<AnalisisLaboratorio, Long> {
    List<AnalisisLaboratorio> findByPacienteId(Long pacienteId);
    Page<AnalisisLaboratorio> findByPaciente(Paciente paciente, Pageable pageable);
    Optional<AnalisisLaboratorio> findByIdAndPaciente_Id(Long id, Long pacienteId);
}