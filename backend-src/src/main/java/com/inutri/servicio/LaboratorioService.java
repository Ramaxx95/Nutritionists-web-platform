package com.inutri.servicio;

import com.inutri.dto.laboratorio.*;
import java.util.List;
import org.springframework.data.domain.*;

public interface LaboratorioService {
    List<BiomarcadorResponse> obtenerBiomarcadoresConRangos();
    AnalisisLaboratorioResponse registrarAnalisisParaPaciente(Long pacienteId, AnalisisLaboratorioRequest request, String emailUsuario);
    AnalisisLaboratorioResponse actualizarAnalisisDePaciente(Long pacienteId, Long analisisId, AnalisisLaboratorioRequest request, String emailUsuario);
    void eliminarAnalisisDePaciente(Long pacienteId, Long analisisId, String emailUsuario);
    Page<AnalisisLaboratorioResponse> listarAnalisis(Pageable pageable, Long pacienteId, String emailUsuario);
    AnalisisLaboratorioResponse buscarAnalisis(Long pacienteId, Long analisisId, String emailUsuario);
    List<ComparacionBiomarcadoresResponse> compararBiomarcadores(Long pacienteId, int cantidad, String emailUsuario);
}