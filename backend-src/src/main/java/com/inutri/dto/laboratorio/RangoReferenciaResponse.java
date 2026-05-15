package com.inutri.dto.laboratorio;

import java.math.BigDecimal;

import com.inutri.modelo.enums.GeneroReferencia;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RangoReferenciaResponse {
    private String descripcion;
    private GeneroReferencia genero;
    private BigDecimal valorMin;
    private BigDecimal valorMax;

    public RangoReferenciaResponse(String descripcion, GeneroReferencia genero, BigDecimal valorMin, BigDecimal valorMax) {
        this.descripcion = descripcion;
        this.genero = genero;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
    }
}