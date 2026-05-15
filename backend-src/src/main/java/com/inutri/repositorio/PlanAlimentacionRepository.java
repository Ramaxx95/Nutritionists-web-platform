package com.inutri.repositorio;

import com.inutri.modelo.PlanAlimentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import java.util.Optional;

public interface PlanAlimentacionRepository extends JpaRepository<PlanAlimentacion, Long> {
    Optional<PlanAlimentacion> findByPaciente_IdAndActivoTrue(Long pacienteId);
    Optional<PlanAlimentacion> findByIdAndPaciente_Id(Long id, Long pacienteId);
    Page<PlanAlimentacion> findByPacienteId(Long pacienteId, Pageable pageable);
}