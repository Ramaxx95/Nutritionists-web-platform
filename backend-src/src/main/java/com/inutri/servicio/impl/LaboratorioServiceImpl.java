package com.inutri.servicio.impl;

import com.inutri.dto.laboratorio.*;
import com.inutri.exception.laboratorio.BiomarcadorNoEncontradoException;
import com.inutri.exception.paciente.*;
import com.inutri.exception.usuario.UsuarioNoEncontradoException;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.GeneroReferencia;
import com.inutri.repositorio.*;
import com.inutri.servicio.LaboratorioService;
import com.inutri.validacion.SuscripcionValidator;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaboratorioServiceImpl implements LaboratorioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AnalisisLaboratorioRepository analisisLaboratorioRepository;

    @Autowired
    private ResultadoAnalisisRepository resultadoAnalisisRepository;

    @Autowired
    private BiomarcadorRepository biomarcadorRepository;

    @Override
    public List<BiomarcadorResponse> obtenerBiomarcadoresConRangos() {
        List<Biomarcador> biomarcadores = biomarcadorRepository.findAll();

        return biomarcadores.stream()
                .map(b -> new BiomarcadorResponse(
                        b.getId(),
                        b.getNombre(),
                        b.getUnidad(),
                        b.getCategoria(),
                        b.getRangosReferencia().stream()
                                .map(r -> new RangoReferenciaResponse(
                                        r.getDescripcion(),
                                        r.getSexo(),
                                        r.getValorMin(),
                                        r.getValorMax()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComparacionBiomarcadoresResponse> compararBiomarcadores(Long pacienteId, int cantidad, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));

        SuscripcionValidator.validarEsPremium(usuario);

        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a comparar debe ser mayor a 0.");
        }

        List<ResultadoAnalisis> resultados = resultadoAnalisisRepository.findResultadosByPacienteOrdered(pacienteId);

        Map<Integer, List<ResultadoAnalisis>> agrupados = resultados.stream()
            .collect(Collectors.groupingBy(r -> r.getBiomarcador().getId(), LinkedHashMap::new, Collectors.toList()));

        List<ComparacionBiomarcadoresResponse> respuesta = new ArrayList<>();

        for (List<ResultadoAnalisis> lista : agrupados.values()) {
            List<ResultadoAnalisis> ultimos = lista.stream()
                .limit(cantidad)
                .toList();

            if (ultimos.isEmpty()) continue;

            Biomarcador biomarcador = ultimos.get(0).getBiomarcador();
            RangoReferencia rango = obtenerRangoReferencia(biomarcador, paciente);

            List<ValorComparacionResponse> valores = ultimos.stream()
                .map(r -> new ValorComparacionResponse(r.getAnalisis().getFecha(), r.getValor()))
                .toList();

            RangoReferenciaResponse rangoBiomarcador = rango != null
                ? new RangoReferenciaResponse(rango.getDescripcion(), rango.getSexo(), rango.getValorMin(), rango.getValorMax())
                : null;

            respuesta.add(new ComparacionBiomarcadoresResponse(
                biomarcador.getNombre(),
                biomarcador.getUnidad(),
                rangoBiomarcador,
                valores
            ));
        }
        return respuesta;
    }

    private RangoReferencia obtenerRangoReferencia(Biomarcador biomarcador, Paciente paciente) {
        String sexoPaciente = paciente.getSexo() != null ? paciente.getSexo().trim().toLowerCase() : "";

        return biomarcador.getRangosReferencia().stream()
            .filter(r -> r.getSexo() == GeneroReferencia.AMBOS ||
                     (r.getSexo() == GeneroReferencia.M && sexoPaciente.equals("masculino")) ||
                     (r.getSexo() == GeneroReferencia.F && sexoPaciente.equals("femenino")))
            .findFirst()
            .orElse(null);
    }

    @Override
    @Transactional
    public AnalisisLaboratorioResponse registrarAnalisisParaPaciente(Long pacienteId, AnalisisLaboratorioRequest request, String emailUsuario) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
            SuscripcionValidator.validarEsPremium(usuario);
            Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));
            if (request.getFecha() == null) {
                throw new DatosPacienteInvalidosException("La fecha del análisis es obligatoria.");
            }
            if (request.getFecha().isAfter(LocalDate.now())) {
                throw new DatosPacienteInvalidosException("La fecha del análisis no puede ser futura.");
            }

            AnalisisLaboratorio analisis = new AnalisisLaboratorio();
            analisis.setFecha(request.getFecha());
            analisis.setNotas(request.getNotas());
            analisis.setPaciente(paciente);

            List<ResultadoAnalisis> resultados = new ArrayList<>();
            if (request.getResultados() != null) {
                for (ResultadoAnalisisRequest resultadoDto : request.getResultados()) {
                    Biomarcador biomarcador = biomarcadorRepository.findById(resultadoDto.getBiomarcadorId())
                            .orElseThrow(() -> new BiomarcadorNoEncontradoException("Biomarcador con ID " + resultadoDto.getBiomarcadorId() + " no encontrado."));

                    ResultadoAnalisis resultado = new ResultadoAnalisis();
                    resultado.setAnalisis(analisis);
                    resultado.setBiomarcador(biomarcador);
                    resultado.setValor(resultadoDto.getValor());
                    resultados.add(resultado);
                }
            }

            analisis.setResultados(resultados);
            analisisLaboratorioRepository.save(analisis);
            List<ResultadoAnalisisResponse> resultadosResponse = resultados.stream()
                .map(r -> new ResultadoAnalisisResponse(
                        r.getBiomarcador().getNombre(),
                        r.getValor(),
                        r.getBiomarcador().getUnidad()))
                .toList();

            return new AnalisisLaboratorioResponse(
                analisis.getId(),
                analisis.getFecha(),
                analisis.getNotas(),
                resultadosResponse
            );
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("Duplicate entry")) {
                throw new DatosPacienteInvalidosException("Ya existe un análisis para este paciente con la misma fecha.");
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public AnalisisLaboratorioResponse actualizarAnalisisDePaciente(Long pacienteId, Long analisisId, AnalisisLaboratorioRequest request, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
        SuscripcionValidator.validarEsPremium(usuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        AnalisisLaboratorio analisis = analisisLaboratorioRepository.findByIdAndPaciente_Id(analisisId, paciente.getId())
            .orElseThrow(() -> new DatosPacienteInvalidosException("Análisis no encontrado o no pertenece al paciente."));

        if (request.getFecha() == null || request.getFecha().isAfter(LocalDate.now())) {
            throw new DatosPacienteInvalidosException("Fecha inválida para el análisis.");
        }

        analisis.setFecha(request.getFecha());
        analisis.setNotas(request.getNotas());
        analisis.getResultados().clear();

        List<ResultadoAnalisis> nuevosResultados = new ArrayList<>();
        for (ResultadoAnalisisRequest resultadoDto : request.getResultados()) {
            Biomarcador biomarcador = biomarcadorRepository.findById(resultadoDto.getBiomarcadorId())
                .orElseThrow(() -> new BiomarcadorNoEncontradoException("Biomarcador con ID " + resultadoDto.getBiomarcadorId() + " no encontrado."));

            ResultadoAnalisis resultado = new ResultadoAnalisis();
            resultado.setAnalisis(analisis);
            resultado.setBiomarcador(biomarcador);
            resultado.setValor(resultadoDto.getValor());
            nuevosResultados.add(resultado);
        }

        analisis.getResultados().addAll(nuevosResultados);
        analisisLaboratorioRepository.save(analisis);

        List<ResultadoAnalisisResponse> resultadosResponse = nuevosResultados.stream()
            .map(r -> new ResultadoAnalisisResponse(
                r.getBiomarcador().getNombre(),
                r.getValor(),
                r.getBiomarcador().getUnidad()))
            .toList();

        return new AnalisisLaboratorioResponse(
            analisis.getId(),
            analisis.getFecha(),
            analisis.getNotas(),
            resultadosResponse
        );
    }

    @Override
    @Transactional
    public void eliminarAnalisisDePaciente(Long pacienteId, Long analisisId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));        
        SuscripcionValidator.validarEsPremium(usuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        AnalisisLaboratorio analisis = analisisLaboratorioRepository.findByIdAndPaciente_Id(analisisId, paciente.getId())
            .orElseThrow(() -> new DatosPacienteInvalidosException("Análisis no encontrado o no pertenece al paciente."));

        analisisLaboratorioRepository.delete(analisis);
    }

    @Override
    public Page<AnalisisLaboratorioResponse> listarAnalisis(Pageable pageable, Long pacienteId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
        SuscripcionValidator.validarEsPremium(usuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));
    
        Page<AnalisisLaboratorio> pageAnalisis = analisisLaboratorioRepository.findByPaciente(paciente, pageable);
    
        Page<AnalisisLaboratorioResponse> pageResponse = pageAnalisis.map(analisis -> {
            List<ResultadoAnalisisResponse> resultadosResponse = analisis.getResultados().stream()
                .map(r -> new ResultadoAnalisisResponse(
                        r.getBiomarcador().getNombre(),
                        r.getValor(),
                        r.getBiomarcador().getUnidad()))
                .toList();
            return new AnalisisLaboratorioResponse(
                analisis.getId(),
                analisis.getFecha(),
                analisis.getNotas(),
                resultadosResponse
            );
        });

        return pageResponse;
    }

    @Override
    public AnalisisLaboratorioResponse buscarAnalisis(Long pacienteId, Long analisisId, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
        SuscripcionValidator.validarEsPremium(usuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));
        AnalisisLaboratorio analisis = analisisLaboratorioRepository.findByIdAndPaciente_Id(analisisId, paciente.getId())
            .orElseThrow(() -> new DatosPacienteInvalidosException("Análisis no encontrado o no pertenece al paciente."));

        List<ResultadoAnalisisResponse> resultadosResponse = analisis.getResultados().stream()
            .map(r -> new ResultadoAnalisisResponse(
                r.getBiomarcador().getNombre(),
                r.getValor(),
                r.getBiomarcador().getUnidad()))
            .toList();

        return new AnalisisLaboratorioResponse(
            analisis.getId(),
            analisis.getFecha(),
            analisis.getNotas(),
            resultadosResponse
        );
    }
}