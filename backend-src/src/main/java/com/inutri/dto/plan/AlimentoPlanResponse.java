package com.inutri.dto.plan;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor
public class AlimentoPlanResponse {
    private String categoria;
    private String nombre;
    private Double cantidad;
    private BigDecimal kcal;
    private BigDecimal carbohidratos;
    private BigDecimal proteinas;
    private BigDecimal grasas;
    private BigDecimal sodio;
    private BigDecimal colesterol;
    private BigDecimal azucarAgregado;
    private BigDecimal fibraAlimentaria;

    public AlimentoPlanResponse(String categoria, String nombre, Double cantidad, BigDecimal kcal, BigDecimal carbohidratos, BigDecimal proteinas, BigDecimal grasas, BigDecimal sodio, BigDecimal colesterol, BigDecimal azucarAgregado, BigDecimal fibraAlimentaria) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.kcal = kcal;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.sodio = sodio;
        this.colesterol = colesterol;
        this.azucarAgregado = azucarAgregado;
        this.fibraAlimentaria = fibraAlimentaria;
    }
}