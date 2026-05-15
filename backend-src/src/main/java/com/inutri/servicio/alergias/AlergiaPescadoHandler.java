package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlergiaPescadoHandler implements AlergiaHandler {

    private final AlimentoRepository alimentoRepository;

    public AlergiaPescadoHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos() {
        return alimentoRepository.findByCategoriaIgnoreCase("Pescados y mariscos");
    }
}