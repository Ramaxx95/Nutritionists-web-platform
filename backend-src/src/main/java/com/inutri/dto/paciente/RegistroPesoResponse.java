package com.inutri.dto.paciente;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegistroPesoResponse {
    private LocalDate fecha;
    private BigDecimal peso;

    public RegistroPesoResponse(LocalDate fecha, BigDecimal peso) {
        this.fecha = fecha;
        this.peso = peso;
    }
}