package com.inutri.dto.patologia;

import java.math.BigDecimal;
import java.util.List;

import lombok.*;

@Getter
@NoArgsConstructor
public class CriterioResponse {
    private String descripcion;
    private String tipoCriterio;
    private BigDecimal limite;
    private String comparacion;
    private String unidad;
    private String nutriente;
    private String flagObjetivo;
    private List<String> categorias;
    private boolean critico;

    public CriterioResponse (String descripcion, String tipoCriterio, BigDecimal limite, String comparacion, String unidad, String nutriente, String flagObjetivo, List<String> categorias, boolean critico) {
        this.descripcion = descripcion;
        this.tipoCriterio = tipoCriterio;
        this.limite = limite;
        this.comparacion = comparacion;
        this.unidad = unidad;
        this.nutriente = nutriente;
        this.flagObjetivo = flagObjetivo;
        this.categorias = categorias;
        this.critico = critico;
    }
}