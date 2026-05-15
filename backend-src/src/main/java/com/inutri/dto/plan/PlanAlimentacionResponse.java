package com.inutri.dto.plan;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

import com.inutri.modelo.enums.TipoDistribucion;
import com.inutri.modelo.enums.EcuacionGER;

import lombok.*;

@Getter
@NoArgsConstructor
public class PlanAlimentacionResponse {
    private  Long id;
    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EcuacionGER ecuacion;
    private TipoDistribucion distribucion;
    private BigDecimal kcalObjetivo;
    private BigDecimal carbohidratosObjetivo;
    private BigDecimal proteinasObjetivo;
    private BigDecimal grasasObjetivo;
    private boolean activo;
    private List<AlternativaResponse> alternativas;

    public PlanAlimentacionResponse (Long id, String objetivo, LocalDate fechaInicio, LocalDate fechaFin, EcuacionGER ecuacion, TipoDistribucion distribucion, BigDecimal kcalObjetivo, BigDecimal carbohidratosObjetivo, BigDecimal proteinasObjetivo, BigDecimal grasasObjetivo, boolean activo, List<AlternativaResponse> alternativas) {
        this.id = id;
        this.objetivo = objetivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ecuacion = ecuacion;
        this.distribucion = distribucion;
        this.kcalObjetivo = kcalObjetivo;
        this.carbohidratosObjetivo = carbohidratosObjetivo;
        this.proteinasObjetivo = proteinasObjetivo;
        this.grasasObjetivo = grasasObjetivo;
        this.activo = activo;
        this.alternativas = alternativas;
    }
}