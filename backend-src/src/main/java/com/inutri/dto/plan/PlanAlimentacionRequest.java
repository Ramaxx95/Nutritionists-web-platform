package com.inutri.dto.plan;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.inutri.modelo.enums.TipoDistribucion;
import com.inutri.modelo.enums.EcuacionGER;

@Getter
@NoArgsConstructor
public class PlanAlimentacionRequest {

    @NotBlank(message = "El objetivo del plan no puede estar vacío.")
    private String objetivo;

    @NotNull
    private LocalDate fechaInicio;

    private EcuacionGER ecuacion;
    private TipoDistribucion distribucion;

    @NotNull
    private List<AlternativaRequest> alternativas;

    public PlanAlimentacionRequest(String objetivo, LocalDate fechaInicio, EcuacionGER ecuacion, TipoDistribucion distribucion, List<AlternativaRequest> alternativas) {
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.ecuacion = ecuacion;
        this.distribucion = distribucion;
        this.alternativas = alternativas;
    }
}