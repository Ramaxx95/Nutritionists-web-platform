package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import java.util.List;

public interface AlergiaHandler {
    List<Alimento> obtenerAlimentosAlergenos();
}