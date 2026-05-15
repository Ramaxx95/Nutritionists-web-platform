package com.inutri.dto.laboratorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ValorComparacionResponse {
    private LocalDate fecha;
    private BigDecimal valor;

    public ValorComparacionResponse(LocalDate fecha, BigDecimal valor) {
        this.fecha = fecha;
        this.valor = valor;
    }
}