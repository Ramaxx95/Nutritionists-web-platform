package com.inutri.servicio;

import com.inutri.dto.sesion.*;
import com.inutri.modelo.enums.PeriodoAgrupamiento;

import java.time.*;
import java.util.List;

import org.springframework.data.domain.*;

public interface SesionService {
    RegistroSesionResponse registrarSesion(Long pacienteId, RegistroSesionRequest sesionDto, String mailUsuario);
    Page<RegistroSesionResponse> listarSesionesDePaciente(Long pacienteId, String mailUsuario, Pageable pageable);
    Page<RegistroSesionResponse> listarSesionesDelNutricionista(String mailUsuario, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, Pageable pageable);
    RegistroSesionResponse editarSesion(Long pacienteId, Long sesionId, EditarSesionRequest request, String mailUsuario);
    void eliminarSesion(Long pacienteId, Long sesionId, String mailUsuario);
    List<NivelAsistenciaResponse> obtenerResumen(PeriodoAgrupamiento periodo, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario);
    List<NivelAsistenciaResponse> obtenerNivelAsistenciaMensual(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario);
}