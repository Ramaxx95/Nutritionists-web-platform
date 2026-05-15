package com.inutri.servicio.impl;

import com.inutri.dto.alimento.AlergiaAlimentoResponse;
import com.inutri.dto.laboratorio.AnalisisLaboratorioRequest;
import com.inutri.dto.laboratorio.ResultadoAnalisisRequest;
import com.inutri.dto.paciente.*;
import com.inutri.exception.usuario.UsuarioNoEncontradoException;
import com.inutri.exception.laboratorio.BiomarcadorNoEncontradoException;
import com.inutri.exception.paciente.*;
import com.inutri.exception.patologia.PatologiaNoEncontradaException;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.EstadoSesion;
import com.inutri.repositorio.*;
import com.inutri.servicio.PacienteService;
import com.inutri.servicio.alergias.AlergiaHandlerManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PacienteServiceImpl implements PacienteService {
    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PesoPacienteRepository pesoPacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PatologiaRepository patologiaRepository;

    @Autowired
    private AlergiaHandlerManager alergiaHandlerManager;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @Autowired
    private AnalisisLaboratorioRepository analisisLaboratorioRepository;

    @Autowired
    private BiomarcadorRepository biomarcadorRepository;

    @Autowired
    private SesionRepository sesionRepository;

    @Override
    public RegistroPacienteResponse registrar(RegistroPacienteRequest pacienteRequestDTO, String mailUsuario) {
        Map<Alimento, String> alergiasConOrigen = new HashMap<>();
        Paciente paciente = toEntity(pacienteRequestDTO, mailUsuario, alergiasConOrigen);

        paciente = pacienteRepository.save(paciente);

        if (pacienteRequestDTO.getPeso() > 0) {
            PesoPaciente pesoPaciente = new PesoPaciente(paciente, LocalDate.now(), BigDecimal.valueOf(pacienteRequestDTO.getPeso()));
            pesoPacienteRepository.save(pesoPaciente);
        }

        if (pacienteRequestDTO.getAnalisis() != null && !pacienteRequestDTO.getAnalisis().isEmpty()) {
            for (AnalisisLaboratorioRequest analisisDto : pacienteRequestDTO.getAnalisis()) {
                if (analisisDto.getFecha() == null) {
                    throw new IllegalArgumentException("La fecha del análisis es obligatoria.");
                }

                if (analisisDto.getFecha().isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha del análisis no puede ser futura.");
                }

                AnalisisLaboratorio analisis = new AnalisisLaboratorio();
                analisis.setFecha(analisisDto.getFecha());
                analisis.setNotas(analisisDto.getNotas());
                analisis.setPaciente(paciente);

                List<ResultadoAnalisis> resultados = new ArrayList<>();
                for (ResultadoAnalisisRequest resultadoDto : analisisDto.getResultados()) {
                    Biomarcador biomarcador = biomarcadorRepository.findById(resultadoDto.getBiomarcadorId())
                            .orElseThrow(() -> new BiomarcadorNoEncontradoException(
                                "Biomarcador con ID " + resultadoDto.getBiomarcadorId() + " no encontrado."));

                    ResultadoAnalisis resultado = new ResultadoAnalisis();
                    resultado.setAnalisis(analisis);
                    resultado.setBiomarcador(biomarcador);
                    resultado.setValor(resultadoDto.getValor());
                    resultados.add(resultado);
                }

                analisis.setResultados(resultados);
                analisisLaboratorioRepository.save(analisis);
            }
        }

        return toResponse(paciente, alergiasConOrigen);
    }

    @Override
    public RegistroPacienteResponse actualizar(ActualizacionDatosPacienteRequest pacienteRequestDTO, Long id, String mailUsuario) {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(id, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        actualizarAtributosPaciente(paciente, pacienteRequestDTO);
        paciente = pacienteRepository.save(paciente);
        if (pacienteRequestDTO.getPeso() != null && pacienteRequestDTO.getPeso() > 0) {
            BigDecimal nuevoPeso = BigDecimal.valueOf(pacienteRequestDTO.getPeso());
            Optional<PesoPaciente> pesoExistente = pesoPacienteRepository.findByPacienteIdAndFecha(paciente.getId(), LocalDate.now());
            PesoPaciente peso;
            if (pesoExistente.isPresent()) {
                peso = pesoExistente.get();
                peso.setPeso(nuevoPeso);
            } else {
                peso = new PesoPaciente(paciente, LocalDate.now(), nuevoPeso);
            }
            pesoPacienteRepository.save(peso);
        }
        Map<Alimento, String> alergiasConOrigen = reconstruirAlergiasConOrigen(paciente);
        return toResponse(paciente, alergiasConOrigen);
    }

    public Page<RegistroPacienteResponse> buscarPacientesEn(Pageable pageable, String busqueda, String mailUsuario) {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        return pacienteRepository
                .buscarPorNombreOApellido(usuario.getId(), busqueda, pageable)
                .map(p -> toResponse(p, reconstruirAlergiasConOrigen(p)));
    }

    public Page<RegistroPacienteResponse> buscarPacientesPorProximaSesion(Pageable pageable, String mailUsuario, Sort.Direction order){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);

        List<RegistroPacienteResponse> pacientes;
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        if(order.isAscending()){
            pacientes = pacienteRepository
                    .buscarPacientesPorApellidoAscendenteYProximaSesion(usuario.getId(), ahora)
                    .stream().map(p -> toResponse(p, reconstruirAlergiasConOrigen(p))).toList();
        }
        else{
            pacientes = pacienteRepository
                    .buscarPacientesPorApellidoDescendenteYProximaSesion(usuario.getId(), ahora)
                    .stream().map(p -> toResponse(p, reconstruirAlergiasConOrigen(p))).toList();
        }


        int inicioPag = (int) pageable.getOffset();
        int finPag = Math.min((inicioPag + pageable.getPageSize()), pacientes.size());
        List<RegistroPacienteResponse> listaRespuesta = pacientes.subList(inicioPag, finPag);

        return new PageImpl<>(listaRespuesta, pageable, pacientes.size());

    }

    public RegistroPacienteResponse obtenerPaciente(Long id, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(id, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        return toResponse(paciente, reconstruirAlergiasConOrigen(paciente));
    }

    private void actualizarAtributosPaciente(Paciente paciente,ActualizacionDatosPacienteRequest pacienteRequestDTO){
        if (pacienteRequestDTO.getNombre() != null) {
           paciente.setNombre(pacienteRequestDTO.getNombre());    
        }
        if (pacienteRequestDTO.getApellido() != null) {
           paciente.setApellido(pacienteRequestDTO.getApellido());    
        }
        if(pacienteRequestDTO.getTelefono() != null){
           paciente.setTelefono(pacienteRequestDTO.getTelefono());    
        }
        if(pacienteRequestDTO.getMail() != null){
           paciente.setMail(pacienteRequestDTO.getMail());    
        } 
        if(pacienteRequestDTO.getActividadFisica() != null){
           paciente.setActividadFisica(pacienteRequestDTO.getActividadFisica());    
        }                       
        if (pacienteRequestDTO.getSexo() != null) {
           paciente.setSexo(pacienteRequestDTO.getSexo());    
        }
        if (pacienteRequestDTO.getFechaNacimiento() != null) {
           paciente.setFechaNacimiento(pacienteRequestDTO.getFechaNacimiento());    
        }
        if (pacienteRequestDTO.getAltura() != null) {
          if (pacienteRequestDTO.getAltura() <= 0) {
              throw new DatosPacienteInvalidosException("Los datos ingresados no son válidos.");
           }            
           paciente.setAltura(pacienteRequestDTO.getAltura());    
        }
        if (pacienteRequestDTO.getPeso() != null) {
           if (pacienteRequestDTO.getPeso() <= 0) {
              throw new DatosPacienteInvalidosException("Los datos ingresados no son válidos.");
           }
           paciente.setPeso(pacienteRequestDTO.getPeso());    
        }
        if (pacienteRequestDTO.getPesoObjetivo() != null) {
            if (pacienteRequestDTO.getPesoObjetivo() <= 0) {
                throw new DatosPacienteInvalidosException("El peso objetivo debe ser mayor a cero.");
            }
            paciente.setPesoObjetivo(pacienteRequestDTO.getPesoObjetivo());    
        }
        if (pacienteRequestDTO.getObjetivo() != null) {
           paciente.setObjetivo(pacienteRequestDTO.getObjetivo());    
        }
        if (pacienteRequestDTO.getPatologias() != null) {
            paciente.setPatologias(mapearPatologias(pacienteRequestDTO.getPatologias()));
            paciente.setAlergenosPersonalizados(pacienteRequestDTO.getAlergenos() != null ? pacienteRequestDTO.getAlergenos() : new ArrayList<>());
            procesarAlergiasDesdePatologias(paciente, pacienteRequestDTO.getAlergenos());
        }
        if (pacienteRequestDTO.getPreferencias() != null) {
            List<Alimento> gustos = alimentoRepository.findAllById(pacienteRequestDTO.getPreferencias().getGustos());
            List<Alimento> desagrados = alimentoRepository.findAllById(pacienteRequestDTO.getPreferencias().getDesagrados());
            paciente.setAlimentosPreferidos(gustos);
            paciente.setAlimentosRestringidos(desagrados);
        }
    }
    
    private List<Patologia> mapearPatologias(List<Integer> ids) {
        List<Patologia> patologias = patologiaRepository.findAllById(ids);

        if (patologias.size() != ids.size()) {
            List<Integer> encontrados = patologias.stream().map(Patologia::getId).toList();
            List<Integer> noEncontrados = ids.stream()
                .filter(id -> !encontrados.contains(id))
                .toList();
            throw new PatologiaNoEncontradaException("No se encontraron las siguientes patologías: " + noEncontrados);
        }

        return patologias;
    }

    private Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
    }

    private Map<Alimento, String> procesarAlergiasDesdePatologias(Paciente paciente, List<String> alergenosPersonalizados) {
        paciente.getAlergias().clear();
        Map<Alimento, String> alergiasConOrigen = new HashMap<>();

        for (Patologia patologia : paciente.getPatologias()) {
            String nombrePatologia = patologia.getNombre();
            if (nombrePatologia.toLowerCase().contains("alergia")) {
                List<Alimento> alimentosAlergenos;
                if (alergiaHandlerManager.requiereEntrada(nombrePatologia)) {
                    if (alergenosPersonalizados == null || alergenosPersonalizados.isEmpty()) {
                        throw new DatosPacienteInvalidosException("Debe especificar al menos un alimento al que el paciente es alérgico para la patología: " + nombrePatologia);
                    }
                    alimentosAlergenos = alergiaHandlerManager.procesarAlergia(nombrePatologia, alergenosPersonalizados);
                } else {
                    alimentosAlergenos = alergiaHandlerManager.procesarAlergia(nombrePatologia);
                }
                for (Alimento alimento : alimentosAlergenos) {
                    if (!paciente.getAlergias().contains(alimento)) {
                        paciente.agregarAlergia(alimento);
                    }
                    alergiasConOrigen.put(alimento, nombrePatologia);
                }
            }
        }
        return alergiasConOrigen;
    }

    private Map<Alimento, String> reconstruirAlergiasConOrigen(Paciente paciente) {
        Map<Alimento, String> alergias = new HashMap<>();

        for (Patologia patologia : paciente.getPatologias()) {
            String nombrePatologia = patologia.getNombre();
            if (nombrePatologia.toLowerCase().contains("alergia")) {
                List<Alimento> alimentos;
                if (alergiaHandlerManager.requiereEntrada(nombrePatologia)) {
                    alimentos = alergiaHandlerManager.procesarAlergia(nombrePatologia, paciente.getAlergenosPersonalizados());
                } else {
                    alimentos = alergiaHandlerManager.procesarAlergia(nombrePatologia);
                }
                for (Alimento alimento : alimentos) {
                    if (paciente.getAlergias().contains(alimento)) {
                        alergias.put(alimento, nombrePatologia);
                    }
                }
            }
        }
        return alergias;
    }

    @Override
    public HistorialPesoResponse obtenerHistorialPeso(Long pacienteId, LocalDate fechaInicio, LocalDate fechaFin, String mailUsuario) {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
            .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado o no pertenece al usuario."));

        List<PesoPaciente> pesos = pesoPacienteRepository.findByPacienteIdOrderByFechaAsc(pacienteId);
        List<RegistroPesoResponse> historial = pesos.stream()
            .filter(p -> (fechaInicio == null || !p.getFecha().isBefore(fechaInicio)) && (fechaFin == null || !p.getFecha().isAfter(fechaFin)))
            .map(p -> new RegistroPesoResponse(p.getFecha(), p.getPeso()))
            .collect(Collectors.toList());
        BigDecimal pesoObjetivo = paciente.getPesoObjetivo() != null ? BigDecimal.valueOf(paciente.getPesoObjetivo()) : null;

        return new HistorialPesoResponse(pesoObjetivo, historial);
    }

    private Paciente toEntity(RegistroPacienteRequest dto, String mailUsuario, Map<Alimento, String> alergiasConOrigen){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);

        List<Patologia> patologias = new ArrayList<>();
        if (dto.getPatologias() != null && !dto.getPatologias().isEmpty()) {
            patologias = mapearPatologias(dto.getPatologias());
        }

        List<Alimento> gustos = new ArrayList<>();
        List<Alimento> disgustos = new ArrayList<>();
        if(dto.getPreferencias() != null){

            for(Integer idGusto : dto.getPreferencias().getGustos()){
                Optional<Alimento> alimentoPreferido = alimentoRepository.findById(idGusto);
                alimentoPreferido.ifPresent(gustos::add);
            }
            for(Integer idDisgusto : dto.getPreferencias().getDesagrados()){
                Optional<Alimento> alimentoRestringido = alimentoRepository.findById(idDisgusto);
                alimentoRestringido.ifPresent(disgustos::add);
            }
        }

        Paciente paciente = new Paciente(
                dto.getNombre(),
                dto.getApellido(),
                dto.getTelefono(),
                dto.getMail(),
                dto.getActividadFisica(),
                dto.getSexo(),
                dto.getFechaNacimiento(),
                dto.getAltura(),
                dto.getPeso(),
                dto.getObjetivo(),
                dto.getPesoObjetivo(),
                patologias,
                gustos,
                disgustos,
                usuario);
        paciente.setAlergenosPersonalizados(dto.getAlergenos() != null ? dto.getAlergenos() : new ArrayList<>());
        alergiasConOrigen.putAll(procesarAlergiasDesdePatologias(paciente, dto.getAlergenos()));
        return paciente;
    }

    private RegistroPacienteResponse toResponse(Paciente paciente, Map<Alimento, String> alergiasConOrigen) {
        List<String> nombresPatologias = paciente.getPatologias().stream()
            .map(Patologia::getNombre)
            .toList();
        List<AlergiaAlimentoResponse> alergias = paciente.getAlergias().stream()
            .map(alimento -> new AlergiaAlimentoResponse(
                alergiasConOrigen.get(alimento),
                alimento.getNombre()
            ))
            .toList();
        PreferenciasResponse preferencias = new PreferenciasResponse(paciente.getAlimentosPreferidos(), paciente.getAlimentosRestringidos());
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        LocalDateTime ultimaSesion = sesionRepository
            .findTopByPaciente_IdAndFechaHoraBeforeAndEstadoOrderByFechaHoraDesc(paciente.getId(), ahora, EstadoSesion.ASISTIO)
            .map(Sesion::getFechaHora)
            .orElse(null);
        LocalDateTime proximaSesion = sesionRepository
            .findTopByPaciente_IdAndFechaHoraAfterAndEstadoOrderByFechaHoraAsc(paciente.getId(), ahora, EstadoSesion.PENDIENTE)
            .map(Sesion::getFechaHora)
            .orElse(null);

        return new RegistroPacienteResponse(
            paciente.getId(),
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getTelefono(),
            paciente.getMail(),
            paciente.getActividadFisica(),
            paciente.getSexo(),
            paciente.getFechaNacimiento(),
            paciente.getAltura(),
            paciente.getPeso(),
            paciente.getObjetivo(),
            paciente.getPesoObjetivo(),
            nombresPatologias,
            paciente.getAlergenosPersonalizados(),
            alergias,
            preferencias,
            ultimaSesion,
            proximaSesion
        );
    }
}