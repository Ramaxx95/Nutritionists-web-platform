package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlergiaNuezHandler implements AlergiaHandler {

    private final AlimentoRepository alimentoRepository;

    public AlergiaNuezHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos() {
        List<Alimento> conNuez = alimentoRepository.findByNombreContainingIgnoreCase("nuez");
        List<Alimento> conNueces = alimentoRepository.findByNombreContainingIgnoreCase("nueces");

        return Stream.concat(conNuez.stream(), conNueces.stream())
                     .distinct()
                     .collect(Collectors.toList());
    }
}