package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AlergiaManiHandler implements AlergiaHandler {

    private final AlimentoRepository alimentoRepository;

    private static final Pattern PATRON_MANI = Pattern.compile("(?i)(?<!\\p{L})man[ií](?!\\p{L})");

    public AlergiaManiHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos() {
        List<Alimento> posibles = alimentoRepository.findAll();

        return posibles.stream()
                .filter(a -> PATRON_MANI.matcher(a.getNombre()).find())
                .collect(Collectors.toList());
    }
}