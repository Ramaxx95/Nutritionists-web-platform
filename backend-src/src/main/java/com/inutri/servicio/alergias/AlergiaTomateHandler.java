package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AlergiaTomateHandler implements AlergiaHandler {

    private final AlimentoRepository alimentoRepository;

    private static final Pattern PATRON_TOMATE = Pattern.compile("\\btomate\\b", Pattern.CASE_INSENSITIVE);

    public AlergiaTomateHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos() {
        return alimentoRepository.findAll().stream()
                .filter(a -> PATRON_TOMATE.matcher(a.getNombre()).find())
                .collect(Collectors.toList());
    }
}