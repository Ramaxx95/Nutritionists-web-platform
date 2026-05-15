package com.inutri.dto.laboratorio;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ComparacionBiomarcadoresResponse {
    private String nombre;
    private String unidad;
    private RangoReferenciaResponse rangoReferencia;
    private List<ValorComparacionResponse> valores;

    public ComparacionBiomarcadoresResponse(String nombre, String unidad, RangoReferenciaResponse rangoReferencia, List<ValorComparacionResponse> valores) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.rangoReferencia = rangoReferencia;
        this.valores = valores;
    }
}