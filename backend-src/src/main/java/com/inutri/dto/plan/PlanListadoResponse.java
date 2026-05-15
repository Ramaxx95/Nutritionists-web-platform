package com.inutri.dto.plan;

import java.time.LocalDate;

import lombok.*;

@Getter
@NoArgsConstructor
public class PlanListadoResponse {

    private Long id;
    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public PlanListadoResponse(Long id, String objetivo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id = id;
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}