package com.inutri.dto.laboratorio;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor
public class ResultadoAnalisisResponse {
    private String biomarcador;
    private BigDecimal valor;
    private String unidad;

    public ResultadoAnalisisResponse(String biomarcador, BigDecimal valor, String unidad) {
        this.biomarcador = biomarcador;
        this.valor = valor;
        this.unidad = unidad;
    }
}