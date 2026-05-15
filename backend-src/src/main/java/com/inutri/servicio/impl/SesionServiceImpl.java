package com.inutri.servicio.impl;

import com.inutri.dto.sesion.*;
import com.inutri.exception.paciente.PacienteNoEncontradoException;
import com.inutri.exception.sesion.EstadoNoValidoException;
import com.inutri.exception.sesion.FechaPasadaException;
import com.inutri.exception.sesion.SesionNoEncontradaException;
import com.inutri.exception.usuario.UsuarioNoEncontradoException;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.EstadoSesion;
import com.inutri.modelo.enums.PeriodoAgrupamiento;
import com.inutri.repositorio.*;
import com.inutri.servicio.SesionService;

import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SesionServiceImpl implements SesionService {

    private static final List<String> DIAS_ORDENADOS = List.of( "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO");

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PesoPacienteRepository pesoPacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public RegistroSesionResponse registrarSesion(Long pacienteId, RegistroSesionRequest sesionDto, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        Sesion sesion = new Sesion(paciente, sesionDto.getFechaHora(), sesionDto.getNotas(), EstadoSesion.PENDIENTE);

        Sesion sesionGuardada = sesionRepository.save(sesion);

        return toResponse(sesionGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistroSesionResponse> listarSesionesDePaciente(Long pacienteId, String mailUsuario, Pageable pageable) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        return sesionRepository.findByPaciente_IdOrderByFechaHoraDesc(paciente.getId(), pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegistroSesionResponse> listarSesionesDelNutricionista(String mailUsuario, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, Pageable pageable) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        LocalDateTime desde = null;
        if (fechaInicio != null) {
            desde = (horaInicio != null)
                    ? fechaInicio.atTime(horaInicio)
                    : fechaInicio.atStartOfDay();
        }
        LocalDateTime hasta = fechaFin != null ? fechaFin.atTime(LocalTime.MAX) : null;

        Page<Sesion> sesiones = sesionRepository.findByUsuarioAndFecha(usuario.getId(), desde, hasta, pageable);

        return sesiones.map(this::toResponse);
    }

    @Override
    @Transactional
    public RegistroSesionResponse editarSesion(Long pacienteId, Long sesionId, EditarSesionRequest request, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        Sesion sesion = sesionRepository.findByIdAndPaciente_Id(sesionId, paciente.getId())
            .orElseThrow(() -> new SesionNoEncontradaException("Sesión no encontrada para el paciente indicado."));

        if (request.getFechaHora() != null) {
            LocalDateTime nuevaFechaHora = request.getFechaHora();
            LocalDateTime fechaHoraActual = sesion.getFechaHora();

            if (!nuevaFechaHora.equals(fechaHoraActual)) {
                if (nuevaFechaHora.isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))) {
                    throw new FechaPasadaException("La fecha y hora deben ser posteriores a la actual.");
                }
                sesion.setFechaHora(nuevaFechaHora);
            }
        }
        if (request.getNotas() != null) {
            sesion.setNotas(request.getNotas());
        }
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            try {
                EstadoSesion estado = EstadoSesion.valueOf(request.getEstado().toUpperCase());
                sesion.setEstado(estado);
            } catch (IllegalArgumentException e) {
                throw new EstadoNoValidoException("Estado no válido. Los valores permitidos son: " + Arrays.toString(EstadoSesion.values()));
            }
        }
        Sesion sesionEditada = sesionRepository.save(sesion);
        if (request.getPeso() != null && request.getPeso() > 0) {
            LocalDate fecha = sesion.getFechaHora().toLocalDate();

            Optional<PesoPaciente> pesoExistente = pesoPacienteRepository.findByPacienteIdAndFecha(paciente.getId(), fecha);
            BigDecimal peso = BigDecimal.valueOf(request.getPeso());
            PesoPaciente pesoActualizado;
            if (pesoExistente.isPresent()) {
                pesoActualizado = pesoExistente.get();
                pesoActualizado.setPeso(peso);
                pesoActualizado.setSesion(sesion);
            } else {
                pesoActualizado = new PesoPaciente(paciente, fecha, peso);
                pesoActualizado.setSesion(sesion);
            }
            pesoPacienteRepository.save(pesoActualizado);

            LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
            Optional<Sesion> sesionMasReciente = sesionRepository.findTopByPaciente_IdAndFechaHoraLessThanEqualOrderByFechaHoraDesc(paciente.getId(), ahora);

            if (sesionMasReciente.isPresent() && sesionMasReciente.get().getId().equals(sesion.getId())) {
                paciente.setPeso(request.getPeso());
                pacienteRepository.save(paciente);
            }
        }
        return toResponse(sesionEditada);
    }

    @Override
    @Transactional
    public void eliminarSesion(Long pacienteId, Long sesionId, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        Sesion sesion = sesionRepository.findByIdAndPaciente_Id(sesionId, paciente.getId())
            .orElseThrow(() -> new IllegalArgumentException("Sesión no encontrada para el paciente indicado."));

        sesionRepository.delete(sesion);
    }

    @Override
    public List<NivelAsistenciaResponse> obtenerResumen(PeriodoAgrupamiento periodo, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        LocalDateTime desde = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime hasta = fechaFin != null ? fechaFin.atTime(LocalTime.MAX) : null;

        return switch (periodo) {
            case MENSUAL -> procesarResumen(
                sesionRepository.contarSesionesMensualesPorEstadoDeUsuario(usuario.getId(), desde, hasta),
                PeriodoAgrupamiento.MENSUAL
            );
            case ANUAL -> procesarResumen(
                sesionRepository.contarSesionesAnualesPorEstadoDeUsuario(usuario.getId(), desde, hasta),
                PeriodoAgrupamiento.ANUAL
            );
            case SEMANAL -> procesarResumen(
                sesionRepository.contarSesionesSemanalesPorEstadoDeUsuario(usuario.getId(), desde, hasta),
                PeriodoAgrupamiento.SEMANAL
            );
        };
    }

    private List<NivelAsistenciaResponse> procesarResumen(List<Object[]> resultados, PeriodoAgrupamiento periodo) {
        Map<String, NivelAsistenciaResponse> resumen = new TreeMap<>();

        for (Object[] fila : resultados) {
            String clave = switch (periodo) {
                case MENSUAL -> (String) fila[0];
                case ANUAL -> String.valueOf(fila[0]);
                case SEMANAL -> getNombreDiaSemana((Integer) fila[0]);
            };
            EstadoSesion estado = (EstadoSesion) fila[1];
            Long cantidad = (Long) fila[2];
            resumen.putIfAbsent(clave, new NivelAsistenciaResponse(clave, 0, 0, 0, 0));
            NivelAsistenciaResponse dto = resumen.get(clave);

            switch (estado) {
                case ASISTIO -> dto.setAsistencias(dto.getAsistencias() + cantidad.intValue());
                case NO_ASISTIO -> dto.setNoAsistencias(dto.getNoAsistencias() + cantidad.intValue());
                case CANCELADA -> dto.setCanceladas(dto.getCanceladas() + cantidad.intValue());
                case PENDIENTE -> dto.setPendientes(dto.getPendientes() + cantidad.intValue());
            }
        }
        return switch (periodo) {
            case SEMANAL -> DIAS_ORDENADOS.stream()
                .map(resumen::get)
                .filter(Objects::nonNull)
                .toList();
            default -> new ArrayList<>(resumen.values());
        };
    }

    private String getNombreDiaSemana(int diaNumero) {
        return switch (diaNumero) {
            case 1 -> "DOMINGO";
            case 2 -> "LUNES";
            case 3 -> "MARTES";
            case 4 -> "MIÉRCOLES";
            case 5 -> "JUEVES";
            case 6 -> "VIERNES";
            case 7 -> "SÁBADO";
            default -> "DESCONOCIDO";
        };
    }

    @Override
    public List<NivelAsistenciaResponse> obtenerNivelAsistenciaMensual(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(mailUsuario).orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId()).orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        LocalDateTime desde = fechaInicio != null ? fechaInicio.atStartOfDay() : null;
        LocalDateTime hasta = fechaFin != null ? fechaFin.atTime(LocalTime.MAX) : null;
        List<Object[]> resultados = sesionRepository.contarSesionesPorMesYEstadoFiltrandoPorFechas(pacienteId, desde, hasta);
        Map<String, NivelAsistenciaResponse> resumenPorMes = new TreeMap<>();

        for (Object[] fila : resultados) {
            String mes = (String) fila[0];
            EstadoSesion estado = (EstadoSesion) fila[1];
            Long cantidad = (Long) fila[2];
            resumenPorMes.putIfAbsent(mes, new NivelAsistenciaResponse(mes, 0, 0, 0, 0));
            NivelAsistenciaResponse dto = resumenPorMes.get(mes);
            switch (estado) {
                case ASISTIO -> dto.setAsistencias(dto.getAsistencias() + cantidad.intValue());
                case NO_ASISTIO -> dto.setNoAsistencias(dto.getNoAsistencias() + cantidad.intValue());
                case CANCELADA -> dto.setCanceladas(dto.getCanceladas() + cantidad.intValue());
                case PENDIENTE -> dto.setPendientes(dto.getPendientes() + cantidad.intValue());
            }
        }

        return new ArrayList<>(resumenPorMes.values());
    }

    private RegistroSesionResponse toResponse(Sesion sesion) {
        Paciente paciente = sesion.getPaciente();
        Float pesoPaciente = pesoPacienteRepository.findBySesion_Id(sesion.getId()).map(p -> p.getPeso().floatValue()).orElse(null);
        return new RegistroSesionResponse(
                sesion.getId(),
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellido(),
                sesion.getFechaHora(),
                sesion.getNotas(),
                sesion.getEstado(),
                pesoPaciente
        );
    }
}