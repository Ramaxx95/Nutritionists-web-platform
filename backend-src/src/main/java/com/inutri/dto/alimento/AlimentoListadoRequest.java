package com.inutri.dto.alimento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlimentoListadoRequest {
    private String nombre;
    private String categoria;

    private BigDecimal minProteinas;
    private BigDecimal maxProteinas;

    private BigDecimal minCarbohidratos;
    private BigDecimal maxCarbohidratos;

    private BigDecimal minGrasas;
    private BigDecimal maxGrasas;

    private BigDecimal minKcal;
    private BigDecimal maxKcal;

    private BigDecimal minSodio;
    private BigDecimal maxSodio;

    private BigDecimal minColesterol;
    private BigDecimal maxColesterol;
}