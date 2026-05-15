package com.inutri.dto.laboratorio;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@NoArgsConstructor
public class AnalisisLaboratorioRequest {
    @NotNull(message = "La fecha del análisis es obligatoria")
    @PastOrPresent(message = "La fecha del análisis no puede ser futura")
    private LocalDate fecha;

    private String notas;

    @NotEmpty(message = "Debe contener al menos un resultado")
    private List<ResultadoAnalisisRequest> resultados;

    public AnalisisLaboratorioRequest(LocalDate fecha, String notas, List<ResultadoAnalisisRequest> resultados) {
        this.fecha = fecha;
        this.notas = notas;
        this.resultados = resultados;
    }
}