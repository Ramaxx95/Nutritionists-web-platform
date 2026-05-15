package com.inutri.dto.laboratorio;

import java.math.BigDecimal;

import lombok.*;

@Getter
@NoArgsConstructor
public class ResultadoAnalisisRequest {
    private Integer biomarcadorId;
    private BigDecimal valor;

    public ResultadoAnalisisRequest(Integer biomarcadorId, BigDecimal valor) {
        this.biomarcadorId = biomarcadorId;
        this.valor = valor;
    }
}