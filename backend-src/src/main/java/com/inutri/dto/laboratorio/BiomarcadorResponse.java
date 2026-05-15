package com.inutri.dto.laboratorio;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BiomarcadorResponse {
    private Integer id;
    private String nombre;
    private String unidad;
    private String categoria;
    private List<RangoReferenciaResponse> rangos;

    public BiomarcadorResponse(Integer id, String nombre, String unidad, String categoria, List<RangoReferenciaResponse> rangos) {
        this.id = id;
        this.nombre = nombre;
        this.unidad = unidad;
        this.categoria = categoria;
        this.rangos = rangos;
    }
}