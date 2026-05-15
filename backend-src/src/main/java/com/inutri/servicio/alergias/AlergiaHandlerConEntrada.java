package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;

import java.util.List;

public interface AlergiaHandlerConEntrada {
    List<Alimento> obtenerAlimentosAlergenos(List<String> alergenos);
}