package com.inutri.servicio;

import com.inutri.dto.paciente.*;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface PacienteService {
    RegistroPacienteResponse registrar(RegistroPacienteRequest paciente, String mailUsuario);
    RegistroPacienteResponse actualizar(ActualizacionDatosPacienteRequest paciente, Long id, String mailUsuario);
    Page<RegistroPacienteResponse> buscarPacientesEn(Pageable pageable, String busqueda, String mailUsuario);
    Page<RegistroPacienteResponse> buscarPacientesPorProximaSesion(Pageable pageable, String mailUsuario, Sort.Direction order);
    RegistroPacienteResponse obtenerPaciente(Long id, String mailUsuario);
    HistorialPesoResponse obtenerHistorialPeso(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario);
}