package com.inutri.controlador;

import com.inutri.dto.laboratorio.*;
import com.inutri.dto.paciente.*;
import com.inutri.dto.plan.*;
import com.inutri.dto.sesion.EditarSesionRequest;
import com.inutri.dto.sesion.NivelAsistenciaResponse;
import com.inutri.dto.sesion.RegistroSesionRequest;
import com.inutri.dto.sesion.RegistroSesionResponse;
import com.inutri.servicio.LaboratorioService;
import com.inutri.servicio.PacienteService;
import com.inutri.servicio.PlanService;
import com.inutri.servicio.SesionService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PlanService planService;

    @Autowired
    private LaboratorioService laboratorioService;

    @Autowired
    private SesionService sesionService;

    @ApiResponse(responseCode = "201", description = "Paciente registrado exitosamente")
    @PostMapping
    public ResponseEntity<RegistroPacienteResponse> crearPaciente(
            @Valid @RequestBody RegistroPacienteRequest pacienteRequestDTO,
            Authentication authentication) {

        String mailUsuario = authentication.getName();
        RegistroPacienteResponse pacienteRegistrado = pacienteService.registrar(pacienteRequestDTO, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pacienteRegistrado);
    }

    @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente")
    @PutMapping("/{id}")
    public ResponseEntity<RegistroPacienteResponse> actualizarPaciente(
            @Valid @RequestBody ActualizacionDatosPacienteRequest pacienteRequestDTO,
            @PathVariable("id") Long id,
            Authentication authentication) {

        String mailUsuario = authentication.getName();
        RegistroPacienteResponse pacienteActualizado = pacienteService.actualizar(pacienteRequestDTO, id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pacienteActualizado);
    }
    
    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de pacientes")
    @GetMapping
    public ResponseEntity<Page<RegistroPacienteResponse>> buscarPacientes(
            @PageableDefault(size = 10, sort = "apellido", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "busqueda", required = false) String busqueda,
            Authentication authentication) {
        String mailUsuario = authentication.getName();

        if(busqueda == null){
            var order = Objects.requireNonNull(pageable.getSort().getOrderFor("apellido")).getDirection();

            if(order.isAscending()){
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pacienteService.buscarPacientesPorProximaSesion(pageable, mailUsuario, order));
            }
            else if(Objects.requireNonNull(pageable.getSort().getOrderFor("apellido")).getDirection().isDescending()){
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(pacienteService.buscarPacientesPorProximaSesion(pageable, mailUsuario, order));
            }
        }

        Page<RegistroPacienteResponse> pacientes = pacienteService.buscarPacientesEn(pageable, busqueda, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(pacientes);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el paciente solicitado")
    @GetMapping("/{id}")
    public ResponseEntity<RegistroPacienteResponse> buscarPaciente(
            @PathVariable("id") Long id,
            Authentication authentication) {

        String mailUsuario = authentication.getName();
        RegistroPacienteResponse paciente = pacienteService.obtenerPaciente(id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(paciente);
    }

    @ApiResponse(responseCode = "201", description = "Plan de alimentación creado exitosamente")
    @PostMapping("/{id}/planes")
    public ResponseEntity<PlanAlimentacionResponse> crearPlanAlimentacion(@PathVariable("id") Long id,
        @RequestBody @Valid PlanAlimentacionRequest request,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        PlanAlimentacionResponse planCreado = planService.crearPlanParaPaciente(id, request, mailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(planCreado);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de planes del paciente")
    @GetMapping("/{id}/planes")
    public ResponseEntity<Page<PlanListadoResponse>> listarPlanes(@PathVariable("id") Long id,
        @PageableDefault(size = 10, sort = "fechaInicio", direction = Sort.Direction.DESC) Pageable pageable,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        Page<PlanListadoResponse> listado = planService.listarPlanes(pageable, id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(listado);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el plan solicitado")
    @GetMapping("/{pacienteId}/planes/{planId}")
    public ResponseEntity<PlanAlimentacionResponse> buscarPlan(@PathVariable("pacienteId") Long pacienteId, @PathVariable("planId") Long planId,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        PlanAlimentacionResponse planBuscado = planService.buscarPlan(pacienteId, planId, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(planBuscado);
    }

    @ApiResponse(responseCode = "200", description = "Alternativa a plan cargada exitosamente")
    @PutMapping("/{pacienteId}/planes/{planId}/alternativas")
    public ResponseEntity<PlanAlimentacionResponse> crearAlternativa(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @RequestBody @Valid AlternativaRequest alternativa,
            Authentication authentication) {
        String mailUsuario = authentication.getName();
        PlanAlimentacionResponse planActualizado = planService.crearAlternativaPara(pacienteId, planId, mailUsuario, alternativa);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(planActualizado);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve sugerencia de alimentos")
    @PostMapping("/{pacienteId}/planes/sugerir_alimentos")
    public ResponseEntity<SugerenciaAlimentosResponse> sugerirAlimentosConIA(
            @PathVariable Long pacienteId,
            @Valid @RequestBody SugerenciaAlimentosRequest request,
            Authentication authentication) throws Exception{
        String mailUsuario = authentication.getName();
        SugerenciaAlimentosResponse sugerencias = planService.sugerirAlimentos(pacienteId, request, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(sugerencias);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve recetas/menus sugeridas por Inteligencia Artificial")
    @GetMapping("/{pacienteId}/planes/{planId}/{alternativaId}/sugerir_menus")
    public ResponseEntity<SugerenciaMenuResponse> sugerirMenusConIA(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @PathVariable("alternativaId") Long alternativaId,
            @RequestParam(value = "verbose", defaultValue = "false") boolean verbose,
            Authentication authentication) throws Exception {
        String mailUsuario = authentication.getName();
        SugerenciaMenuResponse menusSugeridos = planService.sugerirMenusConIA(
                pacienteId,
                planId,
                alternativaId,
                verbose,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menusSugeridos);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve recetas/menus sugeridas por IA para todas las alternativas del plan")
    @GetMapping("/{pacienteId}/planes/{planId}/sugerir_menus")
    public ResponseEntity<SugerenciaMultiplesMenusResponse> sugerirMultiplesMenusConIA(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @RequestParam(value = "verbose", defaultValue = "false") boolean verbose,
            Authentication authentication) throws Exception {
        String mailUsuario = authentication.getName();
        SugerenciaMultiplesMenusResponse menusSugeridos = planService.sugerirMultiplesMenusConIA(
                pacienteId,
                planId,
                verbose,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menusSugeridos);
    }

    @ApiResponse(responseCode = "201", description = "Se guardo el menu a el plan")
    @PostMapping("/{pacienteId}/planes/{planId}/{alternativaId}/menu")
    public ResponseEntity<MenusDeLaAlternativaResponse> registrarMenu(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @PathVariable("alternativaId") Long alternativaId,
            @Valid @RequestBody RegistrarMenuRequest menu,
            Authentication authentication){
        String mailUsuario = authentication.getName();
        MenusDeLaAlternativaResponse menusSugeridos = planService.registrarMenu(
                pacienteId,
                planId,
                alternativaId,
                menu,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menusSugeridos);
    }

    @ApiResponse(responseCode = "200", description = "Se ha actualizado el menu exitosamente")
    @PutMapping("/{pacienteId}/planes/{planId}/{alternativaId}/menu")
    public ResponseEntity<MenusDeLaAlternativaResponse> actualizarMenu(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @PathVariable("alternativaId") Long alternativaId,
            @Valid @RequestBody RegistrarMenuRequest menu,
            Authentication authentication){
        String mailUsuario = authentication.getName();
        MenusDeLaAlternativaResponse menusSugeridos = planService.actualizarMenu(
                pacienteId,
                planId,
                alternativaId,
                menu,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menusSugeridos);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el menu guardado para el plan")
    @GetMapping("/{pacienteId}/planes/{planId}/{alternativaId}/menu")
    public ResponseEntity<SugerenciaMenuResponse> buscarMenu(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            @PathVariable("alternativaId") Long alternativaId,
            Authentication authentication){
        String mailUsuario = authentication.getName();
        SugerenciaMenuResponse menusSugeridos = planService.obtenerMenu(
                pacienteId,
                planId,
                alternativaId,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menusSugeridos);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelven todos los menus pertinentes al plan")
    @GetMapping("/{pacienteId}/planes/{planId}/menus")
    public ResponseEntity<List<MenusDeLaAlternativaResponse>> buscarMenus(
            @PathVariable("pacienteId") Long pacienteId,
            @PathVariable("planId") Long planId,
            Authentication authentication){
        String mailUsuario = authentication.getName();
        List<MenusDeLaAlternativaResponse> menus = planService.obtenerTodosLosMenus(
                pacienteId,
                planId,
                mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(menus);
    }

    @ApiResponse(responseCode = "201", description = "Análisis guardado exitosamente")
    @PostMapping("/{id}/analisis")
    public ResponseEntity<AnalisisLaboratorioResponse> registrarAnalisis(@PathVariable("id") Long id,
        @RequestBody @Valid AnalisisLaboratorioRequest request,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        AnalisisLaboratorioResponse analisisRegistrado = laboratorioService.registrarAnalisisParaPaciente(id, request, mailUsuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(analisisRegistrado);
    }

    @ApiResponse(responseCode = "200", description = "Análisis actualizado exitosamente")
    @PutMapping("/{pacienteId}/analisis/{analisisId}")
    public ResponseEntity<AnalisisLaboratorioResponse> actualizarAnalisis(@PathVariable("pacienteId") Long pacienteId, @PathVariable("analisisId") Long analisisId,
        @Valid @RequestBody AnalisisLaboratorioRequest request,
        Authentication authentication) {
        String mailUsuario = authentication.getName();

        AnalisisLaboratorioResponse actualizado = laboratorioService.actualizarAnalisisDePaciente(pacienteId, analisisId, request, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(actualizado);
    }

    @ApiResponse(responseCode = "204", description = "Análisis eliminado exitosamente")
    @DeleteMapping("/{pacienteId}/analisis/{analisisId}")
    public ResponseEntity<Void> eliminarAnalisis(
            @PathVariable("pacienteId") Long pacienteId, @PathVariable("analisisId") Long analisisId,
            Authentication authentication) {
        String mailUsuario = authentication.getName();
        
        laboratorioService.eliminarAnalisisDePaciente(pacienteId, analisisId, mailUsuario);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de analisis del paciente")
    @GetMapping("/{id}/analisis")
    public ResponseEntity<Page<AnalisisLaboratorioResponse>> listarAnalisis(@PathVariable("id") Long id,
        @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        Page<AnalisisLaboratorioResponse> listado = laboratorioService.listarAnalisis(pageable, id, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(listado);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el analisis solicitado")
    @GetMapping("/{pacienteId}/analisis/{analisisId}")
    public ResponseEntity<AnalisisLaboratorioResponse> buscarAnalisis(@PathVariable("pacienteId") Long pacienteId, @PathVariable("analisisId") Long analisisId,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        AnalisisLaboratorioResponse analisisBuscado = laboratorioService.buscarAnalisis(pacienteId, analisisId, mailUsuario);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(analisisBuscado);
    }

    @ApiResponse(responseCode = "201", description = "Sesión registrada exitosamente")
    @PostMapping("/{pacienteId}/sesiones")
    public ResponseEntity<RegistroSesionResponse> registrarSesion(
        @PathVariable("pacienteId") Long pacienteId,
        @Valid @RequestBody RegistroSesionRequest request,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        RegistroSesionResponse sesion = sesionService.registrarSesion(pacienteId, request, mailUsuario);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sesion);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve el listado de sesiones del paciente")
    @GetMapping("/{pacienteId}/sesiones")
    public ResponseEntity<Page<RegistroSesionResponse>> listarSesionesDelPaciente(
        @PathVariable("pacienteId") Long pacienteId,
        @PageableDefault(size = 10, sort = "fechaHora", direction = Sort.Direction.DESC) Pageable pageable,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        Page<RegistroSesionResponse> sesiones = sesionService.listarSesionesDePaciente(pacienteId, mailUsuario, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(sesiones);
    }

    @ApiResponse(responseCode = "200", description = "Sesión actualizada exitosamente")
    @PutMapping("/{pacienteId}/sesiones/{sesionId}")
    public ResponseEntity<RegistroSesionResponse> editarSesion(
        @PathVariable("pacienteId") Long pacienteId,
        @PathVariable("sesionId") Long sesionId,
        @Valid @RequestBody EditarSesionRequest request,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        RegistroSesionResponse sesionActualizada = sesionService.editarSesion(pacienteId, sesionId, request, mailUsuario);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(sesionActualizada);
    }

    @ApiResponse(responseCode = "204", description = "Sesión eliminada exitosamente")
    @DeleteMapping("/{pacienteId}/sesiones/{sesionId}")
    public ResponseEntity<Void> eliminarSesion(
        @PathVariable("pacienteId") Long pacienteId,
        @PathVariable("sesionId") Long sesionId,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        sesionService.eliminarSesion(pacienteId, sesionId, mailUsuario);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200", description = "Devuelve el resumen mensual de asistencia del paciente")
    @GetMapping("/{pacienteId}/estadisticas/asistencia")
    public ResponseEntity<List<NivelAsistenciaResponse>> obtenerNivelAsistenciaMensual(
        @PathVariable("pacienteId") Long pacienteId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        List<NivelAsistenciaResponse> resumen = sesionService.obtenerNivelAsistenciaMensual(pacienteId, fechaInicio, fechaFin, mailUsuario);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(resumen);
    }

    @ApiResponse(responseCode = "200", description = "Devuelve el historial de peso del paciente en el rango de fechas indicado")
    @GetMapping("/{pacienteId}/estadisticas/peso")
    public ResponseEntity<HistorialPesoResponse> obtenerHistorialPesoPaciente(
        @PathVariable("pacienteId") Long pacienteId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        HistorialPesoResponse historial = pacienteService.obtenerHistorialPeso(pacienteId, fechaInicio, fechaFin, mailUsuario);
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(historial);
    }

    @ApiResponse(responseCode = "200", description = "Se devuelve la comparación de los biomarcadores")
    @GetMapping("/{pacienteId}/analisis/comparacion")
    public ResponseEntity<List<ComparacionBiomarcadoresResponse>> compararBiomarcadores(
        @PathVariable("pacienteId") Long pacienteId,
        @RequestParam(name = "cantidad", defaultValue = "2") int cantidad,
        Authentication authentication) {
        String mailUsuario = authentication.getName();
        List<ComparacionBiomarcadoresResponse> comparacion = laboratorioService.compararBiomarcadores(pacienteId, cantidad, mailUsuario);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(comparacion);
    }
}