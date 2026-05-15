package com.inutri.dto.plan;

import lombok.Getter;

@Getter
public class SugerenciaAlimentosRequest {

    private final Double totalKcal;
    private final Double totalCarbo;
    private final Double totalProteinas;
    private final Double totalGrasas;

    public SugerenciaAlimentosRequest(Double totalKcal, Double carbohidratos, Double proteinas, Double grasas) {
        this.totalKcal = totalKcal;
        this.totalCarbo = carbohidratos;
        this.totalProteinas = proteinas;
        this.totalGrasas = grasas;
    }
}
