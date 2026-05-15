package com.inutri.dto.alimento;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarAlimentoRequest {
    private String nombre;
    private Integer valorEnergetico;
    private BigDecimal agua;
    private BigDecimal proteinas;
    private BigDecimal grasas;
    private BigDecimal colesterol;
    private BigDecimal saturados;
    private BigDecimal monoinsaturados;
    private BigDecimal polininsaturados;
    private BigDecimal trans;
    private BigDecimal cisLinoleico;
    private BigDecimal cisAlfaLinolenico;
    private BigDecimal araquidonico;
    private BigDecimal eicosapentaenoico;
    private BigDecimal docosahexaenoico;
    private BigDecimal carbohidratosDisponibles;
    private BigDecimal carbohidratosTotales;
    private BigDecimal azucarTotal;
    private BigDecimal azucarAgregado;
    private BigDecimal fibraAlimentaria;
    private BigDecimal alcohol;
    private BigDecimal cenizas;
    private Integer sodio;
    private Integer potasio;
    private Integer calcio;
    private BigDecimal cobre;
    private Integer fosforo;
    private BigDecimal hierro;
    private Integer magnesio;
    private BigDecimal zinc;
    private BigDecimal niacina;
    private Integer folatoEFD;
    private Integer acidoFolico;
    private Integer vitaminaA;
    private Integer retinol;
    private BigDecimal tiamina;
    private BigDecimal riboflavina;
    private BigDecimal vitaminaB12;
    private BigDecimal vitaminaC;
    private BigDecimal vitaminaD;
}