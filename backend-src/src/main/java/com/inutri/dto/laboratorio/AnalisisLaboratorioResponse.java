package com.inutri.dto.laboratorio;

import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Getter
@NoArgsConstructor
public class AnalisisLaboratorioResponse {
    private Long id;
    private LocalDate fecha;
    private String notas;
    private List<ResultadoAnalisisResponse> resultados;

    public AnalisisLaboratorioResponse(Long id,LocalDate fecha, String notas, List<ResultadoAnalisisResponse> resultados) {
        this.id = id;
        this.fecha = fecha;
        this.notas = notas;
        this.resultados = resultados;
    }
}