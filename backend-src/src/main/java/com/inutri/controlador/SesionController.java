package com.inutri.controlador;

import com.inutri.dto.sesion.NivelAsistenciaResponse;
import com.inutri.dto.sesion.RegistroSesionResponse;
import com.inutri.modelo.enums.PeriodoAgrupamiento;
import com.inutri.servicio.SesionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de sesiones del nutricionista")
    @GetMapping
    public ResponseEntity<Page<RegistroSesionResponse>> listarTodasLasSesiones(
            @PageableDefault(size = 10, sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            Authentication authentication) {
        String mailUsuario = authentication.getName();
        Page<RegistroSesionResponse> sesiones = sesionService.listarSesionesDelNutricionista(mailUsuario, fechaInicio, horaInicio, fechaFin, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(sesiones);
    }

    @ApiResponse(responseCode = "200", description = "Devuelve el resumen de asistencia de todos los pacientes del nutricionista segun el período indicado")
    @GetMapping("/estadisticas/asistencia")
    public ResponseEntity<List<NivelAsistenciaResponse>> obtenerResumenDelUsuario(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        @RequestParam(defaultValue = "MENSUAL") PeriodoAgrupamiento periodo,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        List<NivelAsistenciaResponse> resumen = sesionService.obtenerResumen(periodo, fechaInicio, fechaFin, mailUsuario);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(resumen);
    }
}