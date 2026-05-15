package com.inutri.servicio;

import com.inutri.dto.alimento.*;
import java.util.List;
import org.springframework.data.domain.*;

public interface AlimentoService {
    Page<AlimentoListadoResponse> listarAlimentos(Pageable pageable, AlimentoListadoRequest filtros, String mailUsuario);
    AlimentoDetalleResponse obtenerAlimento(Integer id, String mailUsuario);
    AgregarAlimentoResponse agregarAlimentoPersonalizado(AgregarAlimentoRequest alimento, String mailUsuario);
    void eliminarAlimentoPersonalizado(Integer id, String mailUsuario);
    AlimentoDetalleResponse actualizarAlimentoPersonalizado(Integer id, ActualizarAlimentoRequest request, String mailUsuario);
    List<CategoriaListadoResponse> obtenerCategorias();
}