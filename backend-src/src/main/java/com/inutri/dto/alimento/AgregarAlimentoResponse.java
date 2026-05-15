package com.inutri.dto.alimento;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor
public class AgregarAlimentoResponse {
    private Integer id;
    private String categoria;
    private String nombre;
    private Integer kcal;
    private BigDecimal proteinas;
    private BigDecimal carbohidratos;
    private BigDecimal grasas;

    public AgregarAlimentoResponse(Integer id, String categoria, String nombre, Integer kcal, BigDecimal proteinas, BigDecimal carbohidratos, BigDecimal grasas) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }
}