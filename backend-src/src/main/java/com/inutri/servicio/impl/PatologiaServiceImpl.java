package com.inutri.servicio.impl;

import com.inutri.dto.patologia.*;
import com.inutri.repositorio.PatologiaRepository;
import com.inutri.servicio.PatologiaService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatologiaServiceImpl implements PatologiaService {

    @Autowired
    private PatologiaRepository patologiaRepository;

    @Override
    public List<PatologiaResponse> listarConCriterios() {
        return patologiaRepository.findAll().stream()
            .map(p -> new PatologiaResponse(
                p.getId(),
                p.getNombre(),
                p.getCriterios().stream()
                    .map(c -> {
                        return new CriterioResponse(
                            c.getDescripcion(),
                            c.getTipoCriterio().name(),
                            c.getLimite(),
                            c.getComparacion().getSimbolo(),
                            c.getUnidad(),
                            c.getNutriente(),
                            c.getFlagObjetivo(),
                            c.getCategorias(),
                            c.isCritico()
                        );
                    })
                    .toList()
            ))
            .toList();
    }
}