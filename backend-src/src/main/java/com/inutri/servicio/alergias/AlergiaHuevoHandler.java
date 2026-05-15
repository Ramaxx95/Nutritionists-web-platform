package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlergiaHuevoHandler implements AlergiaHandler {

    private final AlimentoRepository alimentoRepository;

    public AlergiaHuevoHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos() {
        return alimentoRepository.findAll().stream()
            .filter(a -> a.getNombre().toLowerCase().contains("huevo de gallina"))
            .collect(Collectors.toList());
    }
}