package com.inutri.servicio.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inutri.dto.alimento.AlimentoSugerenciaIAResponse;
import com.inutri.dto.plan.*;
import com.inutri.modelo.*;
import com.inutri.modelo.enums.*;
import com.inutri.repositorio.*;
import com.inutri.servicio.PlanService;
import com.inutri.servicio.UsuarioService;
import com.inutri.util.*;

import com.inutri.servicio.ecuacion.*;
import com.inutri.exception.alimento.AlimentoNoEncontradoException;
import com.inutri.exception.paciente.*;
import com.inutri.exception.plan.*;
import com.inutri.exception.usuario.UsosAgotadosException;
import com.inutri.exception.usuario.UsuarioNoEncontradoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.*;

@Service
public class PlanServiceImpl implements PlanService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private AlimentoRepository alimentoRepository;

    @Autowired
    private PlanAlimentacionRepository planRepository;

    @Autowired
    private EcuacionStrategyFactory estrategiaFactory;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    @Transactional
    public PlanAlimentacionResponse crearPlanParaPaciente(Long pacienteId, PlanAlimentacionRequest planRequest, String mailUsuario) {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));

        PlanAlimentacion nuevoPlan = toEntity(paciente, planRequest);
        
        planRepository.findByPaciente_IdAndActivoTrue(pacienteId).ifPresent(planExistente -> {
            planExistente.setActivo(false);
            planRepository.save(planExistente);
        });
        planRepository.save(nuevoPlan);
        return toResponse(nuevoPlan);
    }

    public Page<PlanListadoResponse> listarPlanes(Pageable pageable, Long idPaciente, String mail) {
        Usuario usuario = obtenerUsuarioPorEmail(mail);
        pacienteRepository.findByIdAndUsuario_Id(idPaciente, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            return planRepository.findByPaciente_IdAndActivoTrue(idPaciente)
                .<Page<PlanListadoResponse>>map(plan -> new PageImpl<>(List.of(toListadoResponse(plan))))
                .orElse(Page.empty());
        }
        return planRepository.findByPacienteId(idPaciente, pageable)
                             .map(this::toListadoResponse);
    }

    public PlanAlimentacionResponse buscarPlan(Long pacienteId, Long planId, String mailUsuario) {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion planBuscado = obtenerPlanValido(pacienteId, planId, usuario);
        return toResponse(planBuscado);
    }

    public PlanAlimentacionResponse crearAlternativaPara(Long pacienteId, Long planId, String mailUsuario, AlternativaRequest alternativa){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion planBuscado = obtenerPlanValido(pacienteId, planId, usuario);

        AlternativaPlan nuevaAlternativa = new AlternativaPlan();
        List<ComidaPlan> comidas = parsearComidaDe(alternativa.getComidas(), nuevaAlternativa);
        nuevaAlternativa.setComidas(comidas);
        nuevaAlternativa.setPlan(planBuscado);

        planBuscado.agregarAlternativa(nuevaAlternativa);
        return toResponse(planRepository.save(planBuscado));
    }

    public SugerenciaAlimentosResponse sugerirAlimentos(
            Long pacienteId,
            SugerenciaAlimentosRequest request,
            String mailUsuario) throws Exception {
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));

        AsistenteIA asistente = new AsistenteIA();
        List<SugerenciaAlimentosEnComidaResponse> comidas = new ArrayList<>(4);
        AjustadorDeMacros ajustador = new AjustadorDeMacros(
                request.getTotalKcal(),
                request.getTotalCarbo(),
                request.getTotalProteinas(),
                request.getTotalGrasas());

        asistente.prepararConversacion();
        String respuesta = hacerConsultaAlimento(request, paciente, asistente);

        armarComida(comidas, respuesta);

        int intentos = 3;
        while (comidas.size() != 4){
            if (intentos <= 0){
                throw new ComidasFaltantesException("La IA no logro hacer una sugerencia de alimentos.");
            }
            intentos--;
            System.out.println("[DEBUG - PlanServiceImpl] La respuesta no tiene 4 alternativas, tiene: " + comidas.size());
            respuesta = arreglarAlimentosRecomendados(comidas.size(), respuesta, asistente);
            armarComida(comidas, respuesta);
        }

        // Ajustar valores obtenidos
        try{
            ajustador.ajustar(comidas, paciente);
        } catch (Exception e) {
            System.out.println("[DEBUG - PlanServiceImpl] El ajustador no encontro combinacion posible para los" +
                    " parametros enviados...");
            System.out.println("[DEBUG - PlanServiceImpl] Enviando plan sin ajustar...");
        }


        asistente.finalizarConversacion();
        Integer tokens = asistente.getTokensUsados();

        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            usuarioService.resetearContadoresSiCorresponde(usuario);
            int usosRestantes = usuario.getUsosPlanIaRestantes();
            if (usosRestantes <= 0) {
                throw new UsosAgotadosException("No quedan usos disponibles para generación de planes con IA este mes.");
            }
            usuario.setUsosPlanIaRestantes(usosRestantes - 1);
            usuarioRepository.save(usuario);
        }

        return new SugerenciaAlimentosResponse(comidas, tokens);
    }

    public SugerenciaMenuResponse sugerirMenusConIA(
            Long pacienteId,
            Long planId,
            Long alternativaId,
            boolean verbose,
            String mailUsuario) throws Exception{
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        List<ComidaPlan> comidas = new ArrayList<>();
        AlternativaPlan alternativa = null;
        for(AlternativaPlan alternativaPlan : plan.getAlternativas()){
            if(Objects.equals(alternativaPlan.getId(), alternativaId)){
                comidas = alternativaPlan.getComidas();
                alternativa = alternativaPlan;
                break;
            }
        }
        if(comidas.isEmpty()){
            throw new AlternativaNoEncontradaException("Alternativa requerida no exite en este plan.");
        }

        AsistenteIA asistente = new AsistenteIA();
        asistente.prepararConversacion();
        List<SugerenciaMenuResponse> menus = new ArrayList<>();
        SugerenciaMenuResponse menuCreado = hacerConsultaMenu(comidas, paciente, verbose, asistente);
        menus.add(menuCreado);

        List<AlternativaPlan> alternativas = new ArrayList<>();
        alternativas.add(alternativa);

        // Necesito mandar las alternativas y menus como una lista
        guardarMenus(plan, alternativas, menus);

        Integer tokens = asistente.getTokensUsados();
        System.out.println("[DEBUG - PlanServiceImpl] Tokens usados: " + tokens);
        asistente.finalizarConversacion();

        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            usuarioService.resetearContadoresSiCorresponde(usuario);
            int usosRestantes = usuario.getUsosMenuIaRestantes();
            if (usosRestantes <= 0) {
                throw new UsosAgotadosException("No quedan usos disponibles para generación de menús con IA este mes.");
            }
            usuario.setUsosMenuIaRestantes(usosRestantes - 1);
            usuarioRepository.save(usuario);
        }

        return menuCreado;

    }

    public SugerenciaMultiplesMenusResponse sugerirMultiplesMenusConIA(Long pacienteId, Long planId, boolean verbose, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        Paciente paciente = pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        List<SugerenciaMenuResponse> menus = new ArrayList<>();
        SugerenciaMultiplesMenusResponse respuesta = new SugerenciaMultiplesMenusResponse();

        try {
            AsistenteIA asistente = new AsistenteIA();
            asistente.prepararConversacion();
            for(AlternativaPlan alternativa : plan.getAlternativas()){
                List<ComidaPlan> comidas = alternativa.getComidas();
                menus.add(hacerConsultaMenu(comidas, paciente, verbose, asistente));
            }
            guardarMenus(plan, plan.getAlternativas(), menus);

            for(AlternativaPlan alternativa : plan.getAlternativas()){
                Long id = alternativa.getId();
                respuesta.agregarMenu(plan.obtenerMenuDe(id), id);
            }
            Integer tokens = asistente.getTokensUsados();
            respuesta.agregarTokensUsados(tokens);

            asistente.finalizarConversacion();
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE) {
            usuarioService.resetearContadoresSiCorresponde(usuario);

            int usosRestantes = usuario.getUsosMenuIaRestantes();
            int cantidadAlternativas = plan.getAlternativas().size();

            if (usosRestantes < cantidadAlternativas) {
                throw new UsosAgotadosException("No quedan usos suficientes para generar menús con IA para todas las alternativas (" + cantidadAlternativas + ").");
            }

            usuario.setUsosMenuIaRestantes(usosRestantes - cantidadAlternativas);
            usuarioRepository.save(usuario);
        }

        return respuesta;
    }

    public MenusDeLaAlternativaResponse registrarMenu(Long pacienteId, Long planId, Long alternativaId, RegistrarMenuRequest menu, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        if(!plan.contieneAlternativa(alternativaId)){ throw new AlternativaNoEncontradaException("La alternativa pasada no corresponde a este plan.");}

        AlternativaPlan alternativa = plan.getAlternativa(alternativaId);
        Menu menuAGuardar;
        if(plan.obtenerMenuDe(alternativaId) == null){
            menuAGuardar = new Menu(menu.getDesayuno(), menu.getAlmuerzo(), menu.getMerienda(), menu.getCena(), alternativa);
        }
        else{
            throw new MenuInvalidoException("Ya existe un menu para la alternativa dada.");
        }

        plan.agregarMenuA(menuAGuardar, alternativaId);
        this.planRepository.save(plan);

        return new MenusDeLaAlternativaResponse(alternativaId, menuAGuardar);
    }

    public MenusDeLaAlternativaResponse actualizarMenu(Long pacienteId, Long planId, Long alternativaId, RegistrarMenuRequest menu, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        if(!plan.contieneAlternativa(alternativaId)){ throw new AlternativaNoEncontradaException("La alternativa pasada no corresponde a este plan.");}

        Menu menuAGuardar;
        if(plan.obtenerMenuDe(alternativaId) == null){
            throw new MenuNoEncontradoException("No existe un menu creado para la alternativa dada.");
        }
        else{
            menuAGuardar = plan.obtenerMenuDe(alternativaId);
            menuAGuardar.setDesayuno(menu.getDesayuno());
            menuAGuardar.setAlmuerzo(menu.getAlmuerzo());
            menuAGuardar.setMerienda(menu.getMerienda());
            menuAGuardar.setCena(menu.getCena());
        }

        plan.agregarMenuA(menuAGuardar, alternativaId);
        this.planRepository.save(plan);

        return new MenusDeLaAlternativaResponse(alternativaId, menuAGuardar);
    }

    public SugerenciaMenuResponse obtenerMenu(Long pacienteId, Long planId, Long alternativaId, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        Menu menuGuardado = plan.obtenerMenuDe(alternativaId);
        if(menuGuardado == null){
            throw new MenuNoEncontradoException("No existe un menu para esta alternativa.");
        }

        SugerenciaMenuResponse respuesta = new SugerenciaMenuResponse();
        respuesta.agregarMenu(menuGuardado);

        return respuesta;
    }

    public List<MenusDeLaAlternativaResponse> obtenerTodosLosMenus(Long pacienteId, Long planId, String mailUsuario){
        Usuario usuario = obtenerUsuarioPorEmail(mailUsuario);
        pacienteRepository.findByIdAndUsuario_Id(pacienteId, usuario.getId())
                .orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado."));
        PlanAlimentacion plan = obtenerPlanValido(pacienteId, planId, usuario);

        List<AlternativaPlan> alternativas = plan.getAlternativas();
        List<MenusDeLaAlternativaResponse> menus = new ArrayList<>();

        for(AlternativaPlan alternativa : alternativas){
            Menu menuAlternativa = alternativa.getMenu();
            if(menuAlternativa == null){
                continue;
            }
            MenusDeLaAlternativaResponse respuesta = new MenusDeLaAlternativaResponse();
            respuesta.cargarAlternativaId(alternativa.getId());
            respuesta.agregarMenu(menuAlternativa);
            menus.add(respuesta);
        }

        if(menus.isEmpty()){
            throw new MenuNoEncontradoException("No existe ningun menu para este plan.");
        }
        return menus;
    }

    private PlanAlimentacion toEntity(Paciente paciente, PlanAlimentacionRequest dto) {
        for(AlternativaRequest alternativa : dto.getAlternativas()){
            validarLas4Comidas(alternativa.getComidas());
        }

        PlanAlimentacion plan = new PlanAlimentacion();
        plan.setPaciente(paciente);
        plan.setActivo(true);
        plan.setObjetivo(dto.getObjetivo());
        plan.setFechaInicio(dto.getFechaInicio());
        EcuacionGER ecuacion = (dto.getEcuacion() != null) ? dto.getEcuacion() : EcuacionGER.ADA;
        plan.setEcuacion(ecuacion);
        BigDecimal kcalRequeridas = calcularKcalRequeridas(paciente, ecuacion);
        plan.setKcalObjetivo(kcalRequeridas);
        TipoDistribucion tipoDistribucion = (dto.getDistribucion() != null) ? dto.getDistribucion() : TipoDistribucion.TRADICIONAL;
        plan.setDistribucion(tipoDistribucion);
        DistribucionMacronutrientes distribucionMacro = DistribucionMacronutrientesFactory.crearPorTipo(tipoDistribucion);
        plan.setCarbohidratosObjetivo(distribucionMacro.calcularGramosCarbohidratos(kcalRequeridas));
        plan.setProteinasObjetivo(distribucionMacro.calcularGramosProteinas(kcalRequeridas));
        plan.setGrasasObjetivo(distribucionMacro.calcularGramosGrasas(kcalRequeridas));

        List<AlternativaPlan> alternativas = new ArrayList<>();
        for(AlternativaRequest alternativaRequest : dto.getAlternativas()){
            AlternativaPlan alternativaPlan = new AlternativaPlan();
            alternativaPlan.setPlan(plan);

            List<ComidaPlan> comidas = parsearComidaDe(alternativaRequest.getComidas(), alternativaPlan);

            alternativaPlan.setComidas(comidas);
            alternativas.add(alternativaPlan);
        }

        plan.setAlternativas(alternativas);
        return plan;
    }

    List<ComidaPlan> parsearComidaDe(List<ComidaRequest> comidas, AlternativaPlan alternativaPlan){

        List<ComidaPlan> comidaParseada = new ArrayList<>();

        for (ComidaRequest comidaRequest : comidas) {
            ComidaPlan comida = new ComidaPlan();
            comida.setTipo(comidaRequest.getTipo());
            comida.setAlternativaPlan(alternativaPlan);

            List<DetalleComida> detalles = new ArrayList<>();
            for (AlimentoPlanRequest detalleRequest : comidaRequest.getAlimentos()) {
                Alimento alimento = alimentoRepository.findById(detalleRequest.getAlimentoId())
                        .orElseThrow(() -> new AlimentoNoEncontradoException("Alimento con ID " + detalleRequest.getAlimentoId() + " no encontrado."));

                DetalleComida detalle = new DetalleComida();
                detalle.setAlimento(alimento);
                detalle.setCantidad(detalleRequest.getCantidad());
                detalle.setComida(comida);

                detalles.add(detalle);
            }

            comida.setDetalles(detalles);
            comidaParseada.add(comida);
        }

        return comidaParseada;
    }

    private PlanAlimentacionResponse toResponse(PlanAlimentacion plan) {

        List<AlternativaResponse> alternativas = new ArrayList<>();

        for (AlternativaPlan alternativa : plan.getAlternativas()) {
            List<ComidaResponse> comidas = new ArrayList<>();
            for(ComidaPlan comida : alternativa.getComidas()){
                ComidaResponse comidaResponse = new ComidaResponse(comida.getTipo(), new ArrayList<>());
                for (DetalleComida detalle : comida.getDetalles()) {
                    Alimento alimento = detalle.getAlimento();
                    BigDecimal gramosConsumidos = BigDecimal.valueOf(detalle.getCantidad());
                    AlimentoPlanResponse alimentoResponse = new AlimentoPlanResponse(
                            alimento.getCategoria(),
                            alimento.getNombre(),
                            detalle.getCantidad(),
                            alimento.calcularKcalAportadas(gramosConsumidos),
                            alimento.calcularCarbohidratosAportados(gramosConsumidos),
                            alimento.calcularProteinasAportadas(gramosConsumidos),
                            alimento.calcularGrasasAportadas(gramosConsumidos),
                            alimento.calcularSodioAportado(gramosConsumidos),
                            alimento.calcularColesterolAportado(gramosConsumidos),
                            alimento.calcularAzucarAportado(gramosConsumidos),
                            alimento.calcularFibraAportada(gramosConsumidos));
                    comidaResponse.getAlimentos().add(alimentoResponse);
                }
                comidas.add(comidaResponse);
            }
            AlternativaResponse alternativaResponse = new AlternativaResponse(alternativa.getId(), comidas);
            alternativas.add(alternativaResponse);
        }

        return new PlanAlimentacionResponse(
                plan.getId(),
                plan.getObjetivo(),
                plan.getFechaInicio(),
                plan.getFechaFin(),
                plan.getEcuacion(),
                plan.getDistribucion(),
                plan.getKcalObjetivo(),
                plan.getCarbohidratosObjetivo(),
                plan.getProteinasObjetivo(),
                plan.getGrasasObjetivo(),
                plan.isActivo(),
                alternativas);
    }
    
    private PlanListadoResponse toListadoResponse(PlanAlimentacion plan) {
        return new PlanListadoResponse(plan.getId(), plan.getObjetivo(), plan.getFechaInicio(), plan.getFechaFin());
    }

    private Usuario obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNoEncontradoException("El mail ingresado no corresponde a ningún usuario."));
    }

    private void validarLas4Comidas(List<ComidaRequest> comidas) {
        Set<TipoComida> tiposRecibidos = comidas.stream()
            .map(ComidaRequest::getTipo)
            .collect(Collectors.toSet());
        if (!tiposRecibidos.containsAll(EnumSet.allOf(TipoComida.class))) {
            Set<TipoComida> faltantes = EnumSet.complementOf(EnumSet.copyOf(tiposRecibidos));
            throw new ComidasFaltantesException("Faltan las siguientes comidas: " + faltantes);
        }
    }

    private void armarComida(List<SugerenciaAlimentosEnComidaResponse> comidas, String respuesta) {

        if(!comidas.isEmpty()){
            comidas.clear();
        }
        FormatoRespuesta.RespuestaAlimentoParaPlan resultadosAlimento = parsearRespuesta(respuesta);
        if(resultadosAlimento != null){
            for(FormatoRespuesta.RespuestaAlimentoParaPlan.Comida comida : resultadosAlimento.comidas){
                comidas.add(convertirComidaEnResponse(comida));
            }
        }

    }

    private FormatoRespuesta.RespuestaAlimentoParaPlan parsearRespuesta(String respuesta){

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(respuesta, FormatoRespuesta.RespuestaAlimentoParaPlan.class);
        } catch (Exception e) {
            return null;
        }

    }

    private BigDecimal calcularKcalRequeridas(Paciente paciente, EcuacionGER ecuacion) {
        if (paciente.getActividadFisica() == null) {
            throw new DatosPacienteFaltantesException("El paciente no tiene asignado un nivel de actividad física.");
        }

        EcuacionStrategy estrategia = estrategiaFactory.getEstrategia(ecuacion);
        estrategia.validarDatosPaciente(paciente);
        BigDecimal ger = estrategia.calcularGER(paciente);
        double factor = obtenerFactorActividadFisica(paciente);
        return ger.multiply(BigDecimal.valueOf(factor));
    }

    private double obtenerFactorActividadFisica(Paciente paciente) {
        ActividadFisica actividad = paciente.getActividadFisica();
        String sexo = paciente.getSexo();

        boolean esHombre = sexo.equalsIgnoreCase("masculino");

        return switch (actividad) {
            case Muy_leve -> 1.3;
            case Leve -> esHombre ? 1.6 : 1.5;
            case Moderada -> esHombre ? 1.7 : 1.6;
            case Intensa -> esHombre ? 2.1 : 1.9;
        };
    }

    private SugerenciaAlimentosEnComidaResponse convertirComidaEnResponse(
            FormatoRespuesta.RespuestaAlimentoParaPlan.Comida comida){

        List<AlimentoSugerenciaIAResponse> alimentos = new ArrayList<>();

        for(FormatoRespuesta.RespuestaAlimentoParaPlan.AlimentoSugerido alimento : comida.alimentos){

            Optional<Alimento> alimentoDB = alimentoRepository.findByNombre(alimento.nombre);
            alimentoDB.ifPresent(value -> alimentos.add(new AlimentoSugerenciaIAResponse(
                    value.getId(),
                    value.getCategoria(),
                    value.getNombre(),
                    value.getValorEnergetico(),
                    value.getProteinas(),
                    value.getCarbohidratosTotales(),
                    value.getGrasas(),
                    value.getColesterol(),
                    value.getSodio(),
                    value.getAzucarAgregado(),
                    value.getFibraAlimentaria(),
                    value.isNoAptoCeliaco(),
                    value.isUltraprocesado(),
                    alimento.cantidad)));
        }

        return new SugerenciaAlimentosEnComidaResponse(comida.tipoComida, alimentos);
    }

    private String hacerConsultaAlimento(SugerenciaAlimentosRequest request, Paciente paciente, AsistenteIA asistente) throws Exception{
        String respuesta = "";

        PromptBuilder prompt = new PromptBuilder()
                .agregarMacros(request.getTotalKcal(), request.getTotalCarbo(), request.getTotalProteinas(), request.getTotalGrasas());

        if(!paciente.getPatologias().isEmpty()){
            prompt = prompt.agregarPatologias(paciente.getPatologias());
        }

        if(paciente.getAlimentosRestringidos() != null){
            ArrayList<String> alimentosRestringidos = new ArrayList<>(paciente.getAlimentosRestringidos()
                    .stream().map(Alimento::getNombre).toList());
            prompt = prompt.agregarAlimentosRestringidos(alimentosRestringidos);
        }

        System.out.println("[DEBUG - PlanServiceImpl] Prompt: " + prompt.getPrompt());
        prompt = prompt.agregarReglasDeConstruccionDePlan().agregarDataAlimentos(asistente.getArchivoEnJson());

        respuesta = asistente.hacerConsulta(prompt.getPrompt(), FormatoRespuesta.RespuestaAlimentoParaPlan.class);
        return respuesta;
    }

    private String arreglarAlimentosRecomendados(int tamanioDado, String respuestaAnterior, AsistenteIA asistente){
        PromptBuilder prompt = new PromptBuilder();
        prompt.completarComidasFaltantes(tamanioDado);
        System.out.println("[DEBUG - PlanServiceImpl] Prompt: " + prompt.getPrompt());
        return asistente.refinarConsulta(respuestaAnterior, prompt.getPrompt());
    }

    private SugerenciaMenuResponse hacerConsultaMenu(List<ComidaPlan> comidas, Paciente paciente, boolean verbose, AsistenteIA asistente) throws Exception{
        Map<TipoComida, ArrayList<String>> alimentos = new HashMap<>();
        for(ComidaPlan comida : comidas){
            ArrayList<String> nombres = new ArrayList<>();
            for(DetalleComida detalle : comida.getDetalles()){
                nombres.add(detalle.getAlimento().getNombre());
            }
            alimentos.put(comida.getTipo(), nombres);
        }

        PromptBuilder prompt = new PromptBuilder().agregarConsultaDeMenu(alimentos, paciente, verbose);
        System.out.println("[DEBUG - PlanServiceImpl] Prompt: " + prompt.getPrompt());
        String respuesta = asistente.hacerConsulta(prompt.getPrompt(), FormatoRespuesta.RespuestaMenuSugerido.class);

        //Deserializamos
        ObjectMapper mapper = new ObjectMapper();
        FormatoRespuesta.RespuestaMenuSugerido menuSugerido = mapper.readValue(respuesta, FormatoRespuesta.RespuestaMenuSugerido.class);

        //Pasamos a DTO
        List<SugerenciaMenuResponse.Menu> menusAEnviar = new ArrayList<>();
        for(FormatoRespuesta.RespuestaMenuSugerido.Menu menu : menuSugerido.menu){
            menusAEnviar.add(new SugerenciaMenuResponse.Menu(menu.tipo, menu.descripcion));
        }

        return new SugerenciaMenuResponse(menusAEnviar);
    }

    private void guardarMenus(PlanAlimentacion plan, List<AlternativaPlan> alternativas, List<SugerenciaMenuResponse> menus){

        assert alternativas.size() == menus.size();
        int iteradorMenus = 0;

        for(AlternativaPlan alternativaPlan : alternativas){
            Menu menuEntry;
            Long alternativaId = alternativaPlan.getId();
            if(plan.obtenerMenuDe(alternativaId) == null){
                menuEntry = new Menu();
            }
            else{
                menuEntry = plan.obtenerMenuDe(alternativaId);
            }
            SugerenciaMenuResponse menuCreado = menus.get(iteradorMenus);

            for(SugerenciaMenuResponse.Menu menu : menuCreado.getMenus()){
                switch (menu.tipoComida()){
                    case DESAYUNO -> menuEntry.setDesayuno(menu.menuSugerido());
                    case ALMUERZO -> menuEntry.setAlmuerzo(menu.menuSugerido());
                    case MERIENDA -> menuEntry.setMerienda(menu.menuSugerido());
                    case CENA -> menuEntry.setCena(menu.menuSugerido());
                }
            }

            menuEntry.setAlternativa(alternativaPlan);

            plan.agregarMenuA(menuEntry, alternativaId);
            iteradorMenus++;
        }

        planRepository.save(plan);
    }

    private PlanAlimentacion obtenerPlanValido(Long pacienteId, Long planId, Usuario usuario) {
        PlanAlimentacion plan = planRepository.findByIdAndPaciente_Id(planId, pacienteId)
            .orElseThrow(() -> new PlanNoEncontradoException("Plan no encontrado."));

        if (usuario.getTipoSubscripcion() == TipoSuscripcion.FREE && !plan.isActivo()) {
            throw new PlanNoEncontradoException("Plan no encontrado.");
        }

        return plan;
    }
}