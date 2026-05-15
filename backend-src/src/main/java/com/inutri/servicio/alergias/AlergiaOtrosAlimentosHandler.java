package com.inutri.servicio.alergias;

import com.inutri.modelo.Alimento;
import com.inutri.repositorio.AlimentoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlergiaOtrosAlimentosHandler implements AlergiaHandlerConEntrada {

    private final AlimentoRepository alimentoRepository;

    public AlergiaOtrosAlimentosHandler(AlimentoRepository alimentoRepository) {
        this.alimentoRepository = alimentoRepository;
    }

    @Override
    public List<Alimento> obtenerAlimentosAlergenos(List<String> alergenos) {
        List<Alimento> todos = alimentoRepository.findAll();
        List<Alimento> resultado = new ArrayList<>();

        for (Alimento alimento : todos) {
            String nombre = alimento.getNombre().toLowerCase();
            for (String alergeno : alergenos) {
                if (nombre.contains(alergeno.toLowerCase())) {
                    resultado.add(alimento);
                    break;
                }
            }
        }

        return resultado;
    }
}