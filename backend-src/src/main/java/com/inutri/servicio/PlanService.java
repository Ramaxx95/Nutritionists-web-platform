package com.inutri.servicio;

import com.inutri.dto.plan.*;
import org.springframework.data.domain.*;

import java.util.List;

public interface PlanService {
    PlanAlimentacionResponse crearPlanParaPaciente(Long idPaciente, PlanAlimentacionRequest plan, String mail);
    Page<PlanListadoResponse> listarPlanes(Pageable pageable, Long idPaciente, String mail);
    PlanAlimentacionResponse buscarPlan(Long pacienteId, Long planId, String mail);
    SugerenciaAlimentosResponse sugerirAlimentos(Long pacienteId, SugerenciaAlimentosRequest request, String mailUsuario) throws Exception;
    SugerenciaMenuResponse sugerirMenusConIA(Long pacienteId, Long planId, Long alternativaId, boolean verbose, String mailUsuario) throws Exception;
    SugerenciaMultiplesMenusResponse sugerirMultiplesMenusConIA(Long pacienteId, Long planId, boolean verbose, String mailUsuario);
    MenusDeLaAlternativaResponse registrarMenu(Long pacienteId, Long planId, Long alternativaId, RegistrarMenuRequest menu, String mailUsuario);
    MenusDeLaAlternativaResponse actualizarMenu(Long pacienteId, Long planId, Long alternativaId, RegistrarMenuRequest menu, String mailUsuario);
    SugerenciaMenuResponse obtenerMenu(Long pacienteId, Long planId, Long alternativaId, String mailUsuario);
    List<MenusDeLaAlternativaResponse> obtenerTodosLosMenus(Long pacienteId, Long planId, String mailUsuario);
    PlanAlimentacionResponse crearAlternativaPara(Long pacienteId, Long planId, String mailUsuario, AlternativaRequest alternativa);
}