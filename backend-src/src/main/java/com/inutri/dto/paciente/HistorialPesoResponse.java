package com.inutri.dto.paciente;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HistorialPesoResponse {
    private BigDecimal pesoObjetivo;
    private List<RegistroPesoResponse> historial;

    public HistorialPesoResponse(BigDecimal pesoObjetivo, List<RegistroPesoResponse> historial) {
        this.pesoObjetivo = pesoObjetivo;
        this.historial = historial;
    }
}